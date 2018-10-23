package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class TemplateRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.047+02:00", comments="Source field: template.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.047+02:00", comments="Source field: template.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.048+02:00", comments="Source field: template.description")
    private String description;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.046+02:00", comments="Source Table: template")
    public TemplateRecord(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.047+02:00", comments="Source field: template.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.047+02:00", comments="Source field: template.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.048+02:00", comments="Source field: template.description")
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
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
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
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
        TemplateRecord other = (TemplateRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated Tue Oct 23 16:13:43 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
}