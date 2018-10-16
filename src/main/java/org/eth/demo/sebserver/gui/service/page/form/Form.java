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
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormBinding;
import org.eth.demo.sebserver.gui.service.widgets.SingleSelection;
import org.json.JSONException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Form extends FormBinding {

    private final Map<String, FormFieldAccessor> formFields = new LinkedHashMap<>();
    private final Map<String, Form> subForms = new LinkedHashMap<>();
    private final Map<String, List<Form>> subLists = new LinkedHashMap<>();
    private final Map<String, Set<String>> groups = new LinkedHashMap<>();

    public void putStatic(final String name, final String value) throws JSONException {
        super.objectRoot.put(name, value);
    }

    public void addToGroup(final String groupName, final String fieldName) {
        if (this.formFields.containsKey(fieldName)) {
            this.groups.computeIfAbsent(groupName, k -> new HashSet<>())
                    .add(fieldName);
        }
    }

    public Form putField(final String name, final Text field) {
        this.formFields.put(name, createAccessor(field));
        return this;
    }

    public void putField(final String name, final Combo field, final String group) {
        if (field instanceof SingleSelection) {
            this.formFields.put(name, createAccessor((SingleSelection) field));
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
                ffa -> ffa.getControl().setVisible(true));
    }

    public void setVisible(final boolean visible, final String group) {
        if (!this.groups.containsKey(group)) {
            return;
        }

        final Set<String> namesSet = this.groups.get(group);
        process(
                name -> namesSet.contains(name),
                ffa -> ffa.getControl().setVisible(visible));
    }

    public void process(
            final Predicate<String> nameFilter,
            final Consumer<FormFieldAccessor> processor) {

        this.formFields.entrySet()
                .stream()
                .filter(entity -> nameFilter.test(entity.getKey()))
                .map(entity -> entity.getValue())
                .forEach(processor);
    }

    @Override
    public void flush() {
        for (final Map.Entry<String, FormFieldAccessor> entry : this.formFields.entrySet()) {
            final FormFieldAccessor accessor = entry.getValue();
            if (accessor.getControl().isVisible()) {
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
    private FormFieldAccessor createAccessor(final Text text) {
        return new FormFieldAccessor() {
            @Override public String getValue() { return text.getText(); }
            @Override public void setValue(final String value) { text.setText(value); }
            @Override public Control getControl() { return text; }
        };
    }
    private FormFieldAccessor createAccessor(final SingleSelection singleSelection) {
        return new FormFieldAccessor() {
            @Override public String getValue() { return singleSelection.getSelectionValue(); }
            @Override public void setValue(final String value) { singleSelection.select(value); }
            @Override public Control getControl() { return singleSelection; }
        };
    }
    //@formatter:on

    public static interface FormFieldAccessor {
        String getValue();

        void setValue(String value);

        Control getControl();
    }

}
