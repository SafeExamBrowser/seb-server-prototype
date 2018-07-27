package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class ConfigurationNodeRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.538+02:00", comments="Source field: configuration_node.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.538+02:00", comments="Source field: configuration_node.owner_id")
    private Long ownerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.539+02:00", comments="Source field: configuration_node.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.539+02:00", comments="Source field: configuration_node.type")
    private String type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.538+02:00", comments="Source Table: configuration_node")
    public ConfigurationNodeRecord(Long id, Long ownerId, String name, String type) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.538+02:00", comments="Source field: configuration_node.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.538+02:00", comments="Source field: configuration_node.owner_id")
    public Long getOwnerId() {
        return ownerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.539+02:00", comments="Source field: configuration_node.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-27T12:52:33.539+02:00", comments="Source field: configuration_node.type")
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_node
     *
     * @mbg.generated Fri Jul 27 12:52:33 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_node
     *
     * @mbg.generated Fri Jul 27 12:52:33 CEST 2018
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
        ConfigurationNodeRecord other = (ConfigurationNodeRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table configuration_node
     *
     * @mbg.generated Fri Jul 27 12:52:33 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }
}