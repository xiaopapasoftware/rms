/**
 * Created by wangganggang on 17/4/25.
 */

var utils = utils || {};

utils.money = {
    /** 金额格式化，三位一瞥 **/
    format: function (amt, pre) {
        if (amt == undefined)
            return "0.00";
        var isfu = false;
        if (parseFloat(amt) < 0) {
            amt = parseFloat(amt) * -1;
            isfu = true;
        }
        pre = pre > 0 && pre <= 20 ? pre : 2;
        amt = parseFloat((amt + "").replace(/[^\d\.-]/g, "")).toFixed(pre) + "";
        var left = amt.split(".")[0].split("").reverse();
        var right = amt.split(".")[1];

        var t = "";
        var fu = "";
        for (var i = 0; i < left.length; i++) {
            t += left[i] + ((i + 1) % 3 == 0 && (i + 1) != left.length ? "," : "");
        }

        fu = isfu ? "-" : "";

        return fu + t.split("").reverse().join("") + "." + right;

    },

    /** 金额去格式化 **/
    unformat: function (money) {
        money = new String(money);
        while (money.indexOf(',', 0) != -1) {
            money = money.replace(',', '');
        }
        return money;
    }
}