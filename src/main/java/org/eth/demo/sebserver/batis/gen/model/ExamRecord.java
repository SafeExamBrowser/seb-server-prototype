package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class ExamRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.lms_setup_id")
    private Long lmsSetupId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.external_uuid")
    private String externalUuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.owner")
    private String owner;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.supporter")
    private String supporter;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.type")
    private String type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.283+01:00", comments="Source Table: exam")
    public ExamRecord(Long id, Long lmsSetupId, String externalUuid, String owner, String supporter, String type) {
        this.id = id;
        this.lmsSetupId = lmsSetupId;
        this.externalUuid = externalUuid;
        this.owner = owner;
        this.supporter = supporter;
        this.type = type;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.lms_setup_id")
    public Long getLmsSetupId() {
        return lmsSetupId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.external_uuid")
    public String getExternalUuid() {
        return externalUuid;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.owner")
    public String getOwner() {
        return owner;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.supporter")
    public String getSupporter() {
        return supporter;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.285+01:00", comments="Source field: exam.type")
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", lmsSetupId=").append(lmsSetupId);
        sb.append(", externalUuid=").append(externalUuid);
        sb.append(", owner=").append(owner);
        sb.append(", supporter=").append(supporter);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
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
        ExamRecord other = (ExamRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLmsSetupId() == null ? other.getLmsSetupId() == null : this.getLmsSetupId().equals(other.getLmsSetupId()))
            && (this.getExternalUuid() == null ? other.getExternalUuid() == null : this.getExternalUuid().equals(other.getExternalUuid()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()))
            && (this.getSupporter() == null ? other.getSupporter() == null : this.getSupporter().equals(other.getSupporter()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLmsSetupId() == null) ? 0 : getLmsSetupId().hashCode());
        result = prime * result + ((getExternalUuid() == null) ? 0 : getExternalUuid().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        result = prime * result + ((getSupporter() == null) ? 0 : getSupporter().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }
}