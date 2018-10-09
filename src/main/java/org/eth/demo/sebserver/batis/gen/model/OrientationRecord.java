package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class OrientationRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.config_attribute_id")
    private Long configAttributeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.view")
    private String view;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.group")
    private String group;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.x_position")
    private Integer xPosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.680+02:00", comments="Source field: orientation.y_position")
    private Integer yPosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source Table: orientation")
    public OrientationRecord(Long id, Long configAttributeId, String view, String group, Integer xPosition, Integer yPosition) {
        this.id = id;
        this.configAttributeId = configAttributeId;
        this.view = view;
        this.group = group;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.config_attribute_id")
    public Long getConfigAttributeId() {
        return configAttributeId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.view")
    public String getView() {
        return view;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.group")
    public String getGroup() {
        return group;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.679+02:00", comments="Source field: orientation.x_position")
    public Integer getxPosition() {
        return xPosition;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-09T09:44:27.680+02:00", comments="Source field: orientation.y_position")
    public Integer getyPosition() {
        return yPosition;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orientation
     *
     * @mbg.generated Tue Oct 09 09:44:27 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configAttributeId=").append(configAttributeId);
        sb.append(", view=").append(view);
        sb.append(", group=").append(group);
        sb.append(", xPosition=").append(xPosition);
        sb.append(", yPosition=").append(yPosition);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orientation
     *
     * @mbg.generated Tue Oct 09 09:44:27 CEST 2018
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrientationRecord other = (OrientationRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getConfigAttributeId() == null ? other.getConfigAttributeId() == null : this.getConfigAttributeId().equals(other.getConfigAttributeId()))
            && (this.getView() == null ? other.getView() == null : this.getView().equals(other.getView()))
            && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()))
            && (this.getxPosition() == null ? other.getxPosition() == null : this.getxPosition().equals(other.getxPosition()))
            && (this.getyPosition() == null ? other.getyPosition() == null : this.getyPosition().equals(other.getyPosition()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orientation
     *
     * @mbg.generated Tue Oct 09 09:44:27 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConfigAttributeId() == null) ? 0 : getConfigAttributeId().hashCode());
        result = prime * result + ((getView() == null) ? 0 : getView().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getxPosition() == null) ? 0 : getxPosition().hashCode());
        result = prime * result + ((getyPosition() == null) ? 0 : getyPosition().hashCode());
        return result;
    }
}