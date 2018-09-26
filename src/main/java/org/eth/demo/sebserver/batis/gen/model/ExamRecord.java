package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class ExamRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source field: exam.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source field: exam.lms_setup_id")
    private Long lmsSetupId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source field: exam.external_uuid")
    private String externalUuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source Table: exam")
    public ExamRecord(Long id, Long lmsSetupId, String externalUuid) {
        this.id = id;
        this.lmsSetupId = lmsSetupId;
        this.externalUuid = externalUuid;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source field: exam.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.630+02:00", comments="Source field: exam.lms_setup_id")
    public Long getLmsSetupId() {
        return lmsSetupId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source field: exam.external_uuid")
    public String getExternalUuid() {
        return externalUuid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Sep 26 09:47:00 CEST 2018
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
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Sep 26 09:47:00 CEST 2018
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
            && (this.getExternalUuid() == null ? other.getExternalUuid() == null : this.getExternalUuid().equals(other.getExternalUuid()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Wed Sep 26 09:47:00 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLmsSetupId() == null) ? 0 : getLmsSetupId().hashCode());
        result = prime * result + ((getExternalUuid() == null) ? 0 : getExternalUuid().hashCode());
        return result;
    }
}