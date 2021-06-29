var optionRecords = "";

layui.use(['layer', 'miniTab', 'form'], function () {
    var $ = layui.jquery,
        form = layui.form
    layer = layui.layer,
        miniTab = layui.miniTab,
        miniTab.listen();

    var un = "";
    // 获取登录人sission信息
    $.ajax({
        url: '/user/getSission',
        type: 'post',
        async: false,
        success: function (data) {
            un = data.username;
        }
    });

    //切换下拉框，切换统计图
    var yuanshuidiTu = echarts.init(document.getElementById('yuanshuidiTu'));
    var shuichangTu = echarts.init(document.getElementById('shuichangTu'));
    // 刷新源水地
    function refreshYuanshuidi() {
        $.ajax({
            url: '/waterPlace/getListBySid',
            type: 'post',
            data: {
                "sid": $('#yuanshuidi option:selected').val(),
                "type": "day"
            },
            success: function (data) {
                var chanshuilv = [];
                var haodianliang = [];
                var gongshuilaing = [];
                var time = [];
                for (var i = 0; i < data.length; i++) {
                    chanshuilv.push(data[i].chanshui);
                    haodianliang.push(data[i].haodian);
                    gongshuilaing.push(data[i].gongshui);
                    time.push(data[i].date);
                }
                optionRecords = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: time
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: '产水率',
                            type: 'line',
                            data: chanshuilv,
                        },
                        {
                            name: '耗电量',
                            type: 'line',
                            data: haodianliang,
                        },
                        {
                            name: '供水量',
                            type: 'line',
                            data: gongshuilaing,
                        }
                    ]
                };
                //重新渲染统计图
                yuanshuidiTu.setOption(optionRecords);
            },
            error: function (e) {
                layer.alert("获取源水地统计图失败！")
            },
        });
    }
    // 刷新水务水厂
    function refreshShuichang() {
        $.ajax({
            url: '/water/getListBySid',
            type: 'post',
            data: {
                "sid": $('#shuichang option:selected').val(),
                "type": "day"
            },
            success: function (data) {
                var chanshuilv = [];
                var haodianliang = [];
                var gongshuilaing = [];
                var time = [];
                for (var i = 0; i < data.length; i++) {
                    chanshuilv.push(data[i].chanshui);
                    haodianliang.push(data[i].haodian);
                    gongshuilaing.push(data[i].gongshui);
                    time.push(data[i].date);
                }
                optionRecords = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        type: 'category',
                        data: time,
                        boundaryGap: false
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                            name: '漏损量',
                            type: 'line',
                            stack: '总量',
                            data: chanshuilv,
                            smooth: true
                        },
                        {
                            name: '耗电量',
                            type: 'line',
                            stack: '总量',
                            data: haodianliang,
                            smooth: true
                        },
                        {
                            name: '供水量',
                            type: 'line',
                            stack: '总量',
                            data: gongshuilaing,
                            smooth: true
                        }
                    ]
                };
                //重新渲染统计图
                shuichangTu.setOption(optionRecords);
            },
            error: function (e) {
                layer.alert("获取水厂统计图失败！")
            },
        });
    }
    //加载源水地下拉框
    $.ajax({
        url: '/waterPlace/getSomeWaterPlace',
        type: 'post',
        data: {},
        success: function (data) {
            for (var i = 0; i < data.data.length; i++) {
                if (i == 0) {
                    $("#yuanshuidi").append(
                        "<option value=\"" + data.data[i].id + "\" selected=\"\">" + data.data[i].name + "</option>");
                } else {
                    $("#yuanshuidi").append(
                        "<option value=\"" + data.data[i].id + "\">" + data.data[i].name + "</option>");
                }
            }
            form.render();
            refreshYuanshuidi();
        },
        error: function (e) {
            layer.alert("获取源水地失败！")
        },
    });
    //加载水务水厂下拉框
    $.ajax({
        url: '/water/getSomeWater',
        type: 'post',
        success: function (data) {
            for (var i = 0; i < data.data.length; i++) {
                if (i == 0) {
                    $("#shuichang").append(
                        "<option value=\"" + data.data[i].id + "\" selected=\"\">" + data.data[i].name + "</option>");
                } else {
                    $("#shuichang").append(
                        "<option value=\"" + data.data[i].id + "\">" + data.data[i].name + "</option>");
                }
            }
            form.render();
            // 刷新水务水厂折线图
            refreshShuichang();
        },
        error: function (e) {
            layer.alert("获取水务水厂失败！")
        },
    });
    //加载用户活跃信息
    $.ajax({
        url: '/user/getActiveUser',
        type: 'post',
        success: function (data) {
            $("#hyzh").text(data.hyzh);
            $("#oneHour").text(data.oneHour);
            $("#oneDay").text(data.oneDay);
            $("#oneMonth").text(data.oneMooth);
            $("#bhyzh").text(data.bhyzh);
        }
    });

    //监听下拉框的选择
    form.on('select(yuanshuidi)', function (data) {
        //选择了之后切换统计图
        refreshYuanshuidi();
    });
    //监听下拉框的选择
    form.on('select(shuichang)', function (data) {
        //选择了之后切换统计图
        refreshShuichang();
    });
    //点击大数据图片跳转
    $("#dashujuButton").on('click',function (data) {
        var url =encodeURI('dashuju.html?username='+un);
        window.location = url;
    });
    // echarts 窗口缩放自适应
    window.onresize = function () {
        yuanshuidiTu.resize();
        shuichangTu.resize();
    }

})

