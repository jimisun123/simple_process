package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 流程模板
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process_template")
public class ProcessTemplate {
    /**
     * 流程模板key
     */
    @TableId(type = IdType.INPUT)
    private String processTemplateKey;

    /**
     * 流程节点列表
     */
    private Set<ProcessNode> processNodeList;

    /**
     * 流程模板状态 1 正常 0关闭
     */
    private Integer processStatus;
}
