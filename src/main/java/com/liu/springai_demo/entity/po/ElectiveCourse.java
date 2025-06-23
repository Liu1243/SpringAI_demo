package com.liu.springai_demo.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 1
 * @since 2025-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("elective_course")
public class ElectiveCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("grade_requirement")
    private Integer gradeRequirement;

    @TableField("type")
    private String type;

    @TableField("credit")
    private BigDecimal credit;

    @TableField("duration_weeks")
    private Integer durationWeeks;

    @TableField("day_of_week")
    private String dayOfWeek;


}
