package com.eking.flow.example;

import com.eking.flow.context.FlowContext;

/**
 * 复杂流程上下文数据
 */
public class ComplexFlowContext extends FlowContext {

    private int priority;              // 优先级 (1-10)
    private String category;           // 分类
    private boolean vipUser;           // 是否VIP用户
    private double amount;             // 金额
    private String status;             // 状态
    private String branch;             // 处理的分支

    // Getters and Setters
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isVipUser() {
        return vipUser;
    }

    public void setVipUser(boolean vipUser) {
        this.vipUser = vipUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "ComplexFlowContext{" +
                "requestId=" + getRequestId() +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", vipUser=" + vipUser +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
