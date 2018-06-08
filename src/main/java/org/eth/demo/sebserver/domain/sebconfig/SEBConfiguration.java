/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig;

import java.util.ArrayList;
import java.util.List;

import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeValue;

public final class SEBConfiguration {

    public final Long id;
    public final Long examId;
    public final String name;

    private final List<AttributeValue> values;

    public SEBConfiguration(final Long id, final Long examId, final String name) {
        this.id = id;
        this.examId = examId;
        this.name = name;
        this.values = new ArrayList<>();
    }

    public void addValue(final AttributeValue value) {
        this.values.add(value);
    }

//    public void fromXMLConfig(final InputStream in) throws ParserConfigurationException, SAXException, IOException {
//        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
//        final org.w3c.dom.Document w3cDocument = documentBuilder.parse(in);
//
//        // TODO
//    }
//
//    public String toXMLConfig() {
//        // TODO
//        return "";
//    }

}
