/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eth.demo.sebserver.domain.sebconfig.attribute.ComplexAttribute;
import org.xml.sax.SAXException;

public class SEBConfiguration {

    public final Long id;
    public final Long examId;
    public final String name;

    private final ComplexAttribute attributes;

    public SEBConfiguration(final Long id, final Long examId, final String name, final ComplexAttribute attributes) {
        this.id = id;
        this.examId = examId;
        this.name = name;
        this.attributes = attributes;
    }

    public void fromXMLConfig(final InputStream in) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        final org.w3c.dom.Document w3cDocument = documentBuilder.parse(in);

        // TODO
    }

    public String toXMLConfig() {
        // TODO
        return "";
    }

}
