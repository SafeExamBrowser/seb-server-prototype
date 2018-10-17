/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormBinding;
import org.eth.demo.sebserver.gui.service.widgets.SingleSelection;
import org.json.JSONException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Form extends FormBinding {

    private final Map<String, FormFieldAccessor<?>> formFields = new LinkedHashMap<>();
    private final Map<String, Form> subForms = new LinkedHashMap<>();
    private final Map<String, List<Form>> subLists = new LinkedHashMap<>();
    private final Map<String, Set<String>> groups = new LinkedHashMap<>();

    public String getValue(final String name) {
        final FormFieldAccessor<?> formFieldAccessor = this.formFields.get(name);
        if (formFieldAccessor != null) {
            return formFieldAccessor.getValue();
        }

        return null;
    }

    public void putStatic(final String name, final String value) throws JSONException {
        super.objectRoot.put(name, value);
    }

    public void addToGroup(final String groupName, final String fieldName) {
        if (this.formFields.containsKey(fieldName)) {
            this.groups.computeIfAbsent(groupName, k -> new HashSet<>())
                    .add(fieldName);
        }
    }

    public Form putField(final String name, final Label label, final Label field) {
        this.formFields.put(name, createAccessor(label, field));
        return this;
    }

    public Form putField(final String name, final Label label, final Text field) {
        this.formFields.put(name, createAccessor(label, field));
        return this;
    }

    public void putField(final String name, final Label label, final Combo field) {
        if (field instanceof SingleSelection) {
            this.formFields.put(name, createAccessor(label, (SingleSelection) field));
        }
    }

    public void putSubForm(final String name, final Form form) {
        this.subForms.put(name, form);
    }

    public Form getSubForm(final String name) {
        return this.subForms.get(name);
    }

    public void addSubForm(final String arrayName, final Form form) {
        final List<Form> array = this.subLists.computeIfAbsent(arrayName, k -> new ArrayList<>());
        array.add(form);
    }

    public Form getSubForm(final String arrayName, final int index) {
        final List<Form> array = this.subLists.get(arrayName);
        if (array == null) {
            return null;
        }

        return array.get(index);
    }

    public void allVisible() {
        process(
                name -> true,
                ffa -> ffa.setVisible(true));
    }

    public void setVisible(final boolean visible, final String group) {
        if (!this.groups.containsKey(group)) {
            return;
        }

        final Set<String> namesSet = this.groups.get(group);
        process(
                name -> namesSet.contains(name),
                ffa -> ffa.setVisible(visible));
    }

    public void process(
            final Predicate<String> nameFilter,
            final Consumer<FormFieldAccessor<?>> processor) {

        this.formFields.entrySet()
                .stream()
                .filter(entity -> nameFilter.test(entity.getKey()))
                .map(entity -> entity.getValue())
                .forEach(processor);
    }

    @Override
    public void flush() {
        for (final Map.Entry<String, FormFieldAccessor<?>> entry : this.formFields.entrySet()) {
            final FormFieldAccessor<?> accessor = entry.getValue();
            if (accessor.control.isVisible()) {
                super.objectRoot.put(entry.getKey(), accessor.getValue());
            }
        }

        for (final Map.Entry<String, Form> entry : this.subForms.entrySet()) {
            final Form subForm = entry.getValue();
            subForm.flush();
            final ObjectNode objectNode = super.mapper.createObjectNode();
            super.objectRoot.set(entry.getKey(), objectNode);
        }

        for (final Map.Entry<String, List<Form>> entry : this.subLists.entrySet()) {
            final List<Form> value = entry.getValue();
            final ArrayNode arrayNode = super.mapper.createArrayNode();
            final int index = 0;
            for (final Form arrayForm : value) {
                arrayForm.flush();
                arrayNode.insert(index, arrayForm.objectRoot);
            }
            super.objectRoot.set(entry.getKey(), arrayNode);
        }
    }

    //@formatter:off
    private FormFieldAccessor<?> createAccessor(final Label label, final Label field) {
        return new FormFieldAccessor<Label>(label, field) {
            @Override public String getValue() { return field.getText(); }
            @Override public void setValue(final String value) { field.setText(value); }
        };
    }
    private FormFieldAccessor<Text> createAccessor(final Label label, final Text text) {
        return new FormFieldAccessor<Text>(label, text) {
            @Override public String getValue() { return text.getText(); }
            @Override public void setValue(final String value) { text.setText(value); }
        };
    }
    private FormFieldAccessor<SingleSelection> createAccessor(
            final Label label,
            final SingleSelection singleSelection) {

        return new FormFieldAccessor<SingleSelection>(label, singleSelection) {
            @Override public String getValue() { return singleSelection.getSelectionValue(); }
            @Override public void setValue(final String value) { singleSelection.select(value); }
        };
    }
    //@formatter:on

    public static abstract class FormFieldAccessor<T extends Control> {

        public final Label label;
        public final T control;

        public FormFieldAccessor(final Label label, final T control) {
            this.label = label;
            this.control = control;
        }

        public abstract String getValue();

        public abstract void setValue(String value);

        public void setVisible(final boolean visible) {
            this.label.setVisible(visible);
            this.control.setVisible(visible);
        }
    }

}
