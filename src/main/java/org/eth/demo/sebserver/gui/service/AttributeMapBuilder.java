/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class AttributeMapBuilder<T extends AttributeMapBuilder<T>> {

    protected final Map<String, String> attributes = new HashMap<>();

    public T attribute(final String name, final String value) {
        this.attributes.put(name, value);
        return (T) this;
    }

    public T exam(final String examId) {
        this.attributes.put(AttributeKeys.EXAM_ID, examId);
        return (T) this;
    }

    public T toState(final String stateId) {
        this.attributes.put(AttributeKeys.STATE_ID, stateId);
        return (T) this;
    }

    public T config(final String configId) {
        this.attributes.put(AttributeKeys.CONFIG_ID, configId);
        return (T) this;
    }

    public T configViewName(final String name) {
        this.attributes.put(AttributeKeys.CONFIG_VIEW_NAME, name);
        return (T) this;
    }

    public T configAttributeNames(final String configAttrs) {
        this.attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_NAMES, configAttrs);
        return (T) this;
    }

    public T singleAttribute() {
        this.attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveValue");
        return (T) this;
    }

    public T tableAttribute() {
        this.attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveTable");
        return (T) this;
    }

    public T attributeValue(final String value) {
        this.attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_VALUE, value);
        return (T) this;
    }

    public T authHeader(final String authHeader) {
        this.attributes.put(AttributeKeys.AUTHORIZATION_HEADER, authHeader);
        return (T) this;
    }

    public T username(final String username) {
        this.attributes.put(AttributeKeys.USER_NAME, username);
        return (T) this;
    }

    public T password(final String password) {
        this.attributes.put(AttributeKeys.PASSWORD, password);
        return (T) this;
    }

    public final void clear() {
        this.attributes.clear();
    }

}
