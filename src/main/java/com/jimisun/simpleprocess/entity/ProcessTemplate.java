package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 流程模版名称
     */
    private String processTemplateName;

    /**
     * 流程模版状态 ref : ProcessTemplateStatusEnum
     */
    private String processTemplateStatus;


    /**
     * 流程模版节点定义
     */
    private String processTemplateNodeDefine;




//  {
//  "process_template_key":"wlclsq",
//  "process_template_name" : "网络策略申请",
//  "process_template_status":"open",
//  "process_node_define": [{
//    "node_type":"start",
//    "node_key" :"wlclsq_start",
//    "node_name" : "网络策略申请开始节点",
//    "next_node_key" : "wlclsq_tjsq",
//    "next_node_name" : "网络策略申请_厅局申请"
//    },{
//    "node_type":"task",
//    "node_key" :"wlclsq_tjsq",
//    "node_name" : "网络策略申请_厅局申请",
//    "node_distribute_type":"group",
//    "node_task_execute_type":"any",
//    "node_actors":"${tj}",
//    "next_node_key" : "wlclsq_sqcl_open_getway",
//    "next_node_name" : "网络策略申请_判断厅局申请材料是否包含59地址"
//  },{
//    "node_type":"open_getway",
//    "node_key" :"wlclsq_pdcl",
//    "node_name" : "网络策略申请_判断厅局申请材料是否包含59地址",
//    "open_getway_script":"${pdcl_conditions}"
//  },{
//    "node_type":"task",
//    "node_key" :"wlclsq_dsjjsh",
//    "node_name" : "网络策略申请_大数据审核",
//    "node_distribute_type":"group",
//    "node_task_execute_type":"any",
//    "node_actors":"dsjj",
//    "next_node_key" : "wlclsq_zssh",
//    "next_node_name" : "网络策略申请_正数审核"
//  },{
//    "node_type":"task",
//    "node_key" :"wlclsq_zssh",
//    "node_name" : "网络策略申请_正数审核",
//    "node_distribute_type":"group",
//    "node_task_execute_type":"any",
//    "node_actors":"dsjj",
//    "next_node_key" : "wlclsq_hzsjjg",
//    "next_node_name" : "网络策略申请_汇总数据结果"
//  },{
//    "node_type":"task",
//    "node_key" :"wlclsq_yyscl",
//    "node_name" : "网络策略申请_运营商处理",
//    "node_distribute_type":"group",
//    "node_task_execute_type":"any",
//    "node_actors":"${yunyinghshang}",
//    "next_node_key" : "wlclsq_hzsjjg",
//    "next_node_name" : "网络策略申请_汇总数据结果"
//  },{
//    "node_type":"close_getway",
//    "node_key" :"wlclsq_hzsjjg",
//    "node_name" : "网络策略申请_汇总数据结果"
//  },{
//    "node_type":"end",
//    "node_key" :"wlclsq_end",
//    "node_name" : "网络策略申请_结束节点"
//    }]
//  }
//

}
