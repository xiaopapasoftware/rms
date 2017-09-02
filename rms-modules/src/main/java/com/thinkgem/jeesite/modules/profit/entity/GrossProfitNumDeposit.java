package com.thinkgem.jeesite.modules.profit.entity;

public class GrossProfitNumDeposit {

    private int start;

    private int end;

    private int size;

    private Double deposit;

    public GrossProfitNumDeposit(int start, int end, Double deposit) {
        this.start = start;
        this.end = end;
        this.deposit = deposit;
        this.size = this.end - this.start + 1;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }
}
