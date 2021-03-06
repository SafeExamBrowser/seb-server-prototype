package org.eth.demo.sebserver.batis.gen.model;

import java.math.BigDecimal;
import javax.annotation.Generated;

public class IndicatorRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.exam_id")
    private Long examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.type")
    private String type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.threshold1")
    private BigDecimal threshold1;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.threshold2")
    private BigDecimal threshold2;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.304+01:00", comments="Source field: indicator.threshold3")
    private BigDecimal threshold3;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source Table: indicator")
    public IndicatorRecord(Long id, Long examId, String type, String name, BigDecimal threshold1, BigDecimal threshold2, BigDecimal threshold3) {
        this.id = id;
        this.examId = examId;
        this.type = type;
        this.name = name;
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
        this.threshold3 = threshold3;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.exam_id")
    public Long getExamId() {
        return examId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.type")
    public String getType() {
        return type;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.threshold1")
    public BigDecimal getThreshold1() {
        return threshold1;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.303+01:00", comments="Source field: indicator.threshold2")
    public BigDecimal getThreshold2() {
        return threshold2;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.304+01:00", comments="Source field: indicator.threshold3")
    public BigDecimal getThreshold3() {
        return threshold3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table indicator
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
        sb.append(", examId=").append(examId);
        sb.append(", type=").append(type);
        sb.append(", name=").append(name);
        sb.append(", threshold1=").append(threshold1);
        sb.append(", threshold2=").append(threshold2);
        sb.append(", threshold3=").append(threshold3);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table indicator
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
        IndicatorRecord other = (IndicatorRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExamId() == null ? other.getExamId() == null : this.getExamId().equals(other.getExamId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getThreshold1() == null ? other.getThreshold1() == null : this.getThreshold1().equals(other.getThreshold1()))
            && (this.getThreshold2() == null ? other.getThreshold2() == null : this.getThreshold2().equals(other.getThreshold2()))
            && (this.getThreshold3() == null ? other.getThreshold3() == null : this.getThreshold3().equals(other.getThreshold3()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table indicator
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamId() == null) ? 0 : getExamId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getThreshold1() == null) ? 0 : getThreshold1().hashCode());
        result = prime * result + ((getThreshold2() == null) ? 0 : getThreshold2().hashCode());
        result = prime * result + ((getThreshold3() == null) ? 0 : getThreshold3().hashCode());
        return result;
    }
}