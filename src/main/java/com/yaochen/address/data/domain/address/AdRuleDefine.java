/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

public class AdRuleDefine {
    /** 规则ID */
    private Integer ruleId;

    /** 规则名 */
    private String ruleName;

    /** 脚本语言 */
    private String scriptingLanguage;

    /** 规则内容 */
    private String ruleContent;

    /** 规则版本号，每次修改时，请将该字段的值加1 */
    private Integer version;

    /** 规则内容是否是返回值 */
    private String returnValue;

    /** 返回值类型简单检查方式：布尔值、数值、不检查 */
    private String returnTest;

    /** 规则使用场景：物品定义使用、行为奖励使用、排行使用等等 */
    private String uleScene;

    /** 描述 */
    private String ruleDesc;

    /** 状态 正常、失效 */
    private String status;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public String getScriptingLanguage() {
        return scriptingLanguage;
    }

    public void setScriptingLanguage(String scriptingLanguage) {
        this.scriptingLanguage = scriptingLanguage == null ? null : scriptingLanguage.trim();
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent == null ? null : ruleContent.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }

    public String getReturnTest() {
        return returnTest;
    }

    public void setReturnTest(String returnTest) {
        this.returnTest = returnTest == null ? null : returnTest.trim();
    }

    public String getUleScene() {
        return uleScene;
    }

    public void setUleScene(String uleScene) {
        this.uleScene = uleScene == null ? null : uleScene.trim();
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}