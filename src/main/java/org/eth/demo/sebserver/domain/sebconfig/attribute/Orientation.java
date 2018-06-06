/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig.attribute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Orientation {

    public static final Orientation NO_ORIENTATION = new Orientation(null, null, 0, 0);

    public final String pageName;
    public final String groupName;
    public final int xPosition;
    public final int yPosition;

    @JsonCreator
    public Orientation(
            @JsonProperty("pageName") final String pageName,
            @JsonProperty("groupName") final String groupName,
            @JsonProperty("xPosition") final int xPosition,
            @JsonProperty("yPosition") final int yPosition) {

        this.pageName = pageName;
        this.groupName = groupName;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public String getPageName() {
        return this.pageName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public int getxPosition() {
        return this.xPosition;
    }

    public int getyPosition() {
        return this.yPosition;
    }

}
