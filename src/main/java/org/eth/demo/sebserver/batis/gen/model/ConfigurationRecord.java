package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;
import org.joda.time.DateTime;

public class ConfigurationRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.configuration_node_id")
    private Long configurationNodeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.version")
    private String version;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.version_date")
    private DateTime versionDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.followup")
    private Boolean followup;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source Table: configuration")
    public ConfigurationRecord(Long id, Long configurationNodeId, String version, DateTime versionDate, Boolean followup) {
        this.id = id;
        this.configurationNodeId = configurationNodeId;
        this.version = version;
        this.versionDate = versionDate;
        this.followup = followup;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.configuration_node_id")
    public Long getConfigurationNodeId() {
        return configurationNodeId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.version")
    public String getVersion() {
        return version;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.version_date")
    public DateTime getVersionDate() {
        return versionDate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-22T11:48:06.446+02:00", comments="Source field: configuration.followup")
    public Boolean getFollowup() {
        return followup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration
     *
     * @mbg.generated Wed Aug 22 11:48:06 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configurationNodeId=").append(configurationNodeId);
        sb.append(", version=").append(version);
        sb.append(", versionDate=").append(versionDate);
        sb.append(", followup=").append(followup);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration
     *
     * @mbg.generated Wed Aug 22 11:48:06 CEST 2018
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
        ConfigurationRecord other = (ConfigurationRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getConfigurationNodeId() == null ? other.getConfigurationNodeId() == null : this.getConfigurationNodeId().equals(other.getConfigurationNodeId()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getVersionDate() == null ? other.getVersionDate() == null : this.getVersionDate().equals(other.getVersionDate()))
            && (this.getFollowup() == null ? other.getFollowup() == null : this.getFollowup().equals(other.getFollowup()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration
     *
     * @mbg.generated Wed Aug 22 11:48:06 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConfigurationNodeId() == null) ? 0 : getConfigurationNodeId().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getVersionDate() == null) ? 0 : getVersionDate().hashCode());
        result = prime * result + ((getFollowup() == null) ? 0 : getFollowup().hashCode());
        return result;
    }
}