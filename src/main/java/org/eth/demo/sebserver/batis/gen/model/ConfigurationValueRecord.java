package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class ConfigurationValueRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.configuration_id")
    private Long configurationId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.configuration_attribute_id")
    private Long configurationAttributeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.list_index")
    private Integer listIndex;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.040+02:00", comments="Source field: configuration_value.value")
    private String value;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.040+02:00", comments="Source field: configuration_value.text")
    private String text;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source Table: configuration_value")
    public ConfigurationValueRecord(Long id, Long configurationId, Long configurationAttributeId, Integer listIndex, String value, String text) {
        this.id = id;
        this.configurationId = configurationId;
        this.configurationAttributeId = configurationAttributeId;
        this.listIndex = listIndex;
        this.value = value;
        this.text = text;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.configuration_id")
    public Long getConfigurationId() {
        return configurationId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.039+02:00", comments="Source field: configuration_value.configuration_attribute_id")
    public Long getConfigurationAttributeId() {
        return configurationAttributeId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.040+02:00", comments="Source field: configuration_value.list_index")
    public Integer getListIndex() {
        return listIndex;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.040+02:00", comments="Source field: configuration_value.value")
    public String getValue() {
        return value;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.040+02:00", comments="Source field: configuration_value.text")
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_value
     *
     * @mbg.generated Tue Oct 23 16:13:43 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configurationId=").append(configurationId);
        sb.append(", configurationAttributeId=").append(configurationAttributeId);
        sb.append(", listIndex=").append(listIndex);
        sb.append(", value=").append(value);
        sb.append(", text=").append(text);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_value
     *
     * @mbg.generated Tue Oct 23 16:13:43 CEST 2018
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
        ConfigurationValueRecord other = (ConfigurationValueRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getConfigurationId() == null ? other.getConfigurationId() == null : this.getConfigurationId().equals(other.getConfigurationId()))
            && (this.getConfigurationAttributeId() == null ? other.getConfigurationAttributeId() == null : this.getConfigurationAttributeId().equals(other.getConfigurationAttributeId()))
            && (this.getListIndex() == null ? other.getListIndex() == null : this.getListIndex().equals(other.getListIndex()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
            && (this.getText() == null ? other.getText() == null : this.getText().equals(other.getText()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_value
     *
     * @mbg.generated Tue Oct 23 16:13:43 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConfigurationId() == null) ? 0 : getConfigurationId().hashCode());
        result = prime * result + ((getConfigurationAttributeId() == null) ? 0 : getConfigurationAttributeId().hashCode());
        result = prime * result + ((getListIndex() == null) ? 0 : getListIndex().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        return result;
    }
}