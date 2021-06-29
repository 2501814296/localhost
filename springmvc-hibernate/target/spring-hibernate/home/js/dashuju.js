var optionRecords;

layui.use(['layer', 'form'], function () {
    var $ = layui.jquery,
        form = layui.form
    layer = layui.layer;
    // 清除掉index.html的内容
    if (top.location != self.location) top.location = self.location;
    // 获取url参数方法
    function getQueryVariable(variable){
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    }
    var un = getQueryVariable('username');
    un =decodeURI(un);

    var yujingTu = echarts.init(document.getElementById('yujing'));
    var gongdanTu = echarts.init(document.getElementById('gongdan'));
    var jiankongTu = echarts.init(document.getElementById('jiankong'));
    var gongshuiTu = echarts.init(document.getElementById('gongshui'));
    var handianTu = echarts.init(document.getElementById('handian'));
    var shuifeiTu = echarts.init(document.getElementById('shuifei'));
    var rightOneTu = echarts.init(document.getElementById('rightOneTu'));
    var rightTwoTu = echarts.init(document.getElementById('rightTwoTu'));
    var rightTwoTu_1 = echarts.init(document.getElementById('rightTwoTu-1'));
    var rightTwoTu_2 = echarts.init(document.getElementById('rightTwoTu-2'));
    var rightTwoTu_3 = echarts.init(document.getElementById('rightTwoTu-3'));
    var rightTwoTu_4 = echarts.init(document.getElementById('rightTwoTu-4'));
    var rightTwoTu_5 = echarts.init(document.getElementById('rightTwoTu-5'));
    var rightThreeTu = echarts.init(document.getElementById('rightThreeTu'));


    // 刷新地图
    function refreshDiTu(type) {
        // 初始化百度地图
        var map = new BMap.Map("container");        //在container容器中创建一个地图,参数container为div的id属性;
        var point = new BMap.Point(108, 33);    //创建点坐标
        map.centerAndZoom(point, 5);                //初始化地图，设置中心点坐标和地图级别
        map.enableScrollWheelZoom();                //激活滚轮调整大小功能
        map.addControl(new BMap.NavigationControl());    //添加控件：缩放地图的控件，默认在左上角；
        map.addControl(new BMap.MapTypeControl());        //添加控件：地图类型控件，默认在右上方；
        map.addControl(new BMap.ScaleControl());        //添加控件：地图显示比例的控件，默认在左下方；
        map.addControl(new BMap.OverviewMapControl());  //添加控件：地图的缩略图的控件，默认在右下方； TrafficControl

        $.ajax({
            url: "/dashuju/getditupoint",
            type: "post",
            data: {"type": type},
            dataType: "json",
            success: function (json) {
                console.log(json);
                if (json.flag == "1") {
                    var result = json.result;
                    var linePoints = [];
                    for (var i = 0; i < result.length; i++) { //循环赋值
                        //显示标注
                        var pt = new BMap.Point(result[i].x, result[i].y);
                        var marker = new BMap.Marker(pt);        // 创建标注
                        map.addOverlay(marker);
                        //设置文本标题显示
                        var label = new BMap.Label(result[i].dw, {offset: new BMap.Size(20, -10)});
                        marker.setLabel(label);
                        //设置点击事件信息窗口  按照官方你文档这样写的，不然就出错
                        var opts = {
                            position: pt,    // 指定文本标注所在的地理位置
                            width: 100,     // 信息窗口宽度
                            height: 50,     // 信息窗口高度
                            offset: new BMap.Size(20, -20), //设置文本偏移量
                            title: "详情", // 信息窗口标题
                        }
                        var info_html = result[i].dw;
                        addClickHandler(info_html, marker, result[i].x, result[i].y);
                    };

                    function addClickHandler(content, marker, x, y) {
                        marker.addEventListener("click", function (e) {
                            map.centerAndZoom(new BMap.Point(x, y), 9);
                            openInfo(content, e)
                            // 刷新右侧统计图
                            var type = $(".active").children().children().text();
                            if (type == '管网') {
                                refreshRightOne(1,content);
                                refreshRightTwo(1,content);
                                refreshRightThree(1,content);
                            }else if (type == '水厂') {
                                refreshRightOne(2,content);
                                refreshRightTwo(2,content);
                                refreshRightThree(2,content);
                            }else if (type == '水源') {
                                refreshRightOne(3,content);
                                refreshRightTwo(3,content);
                                refreshRightThree(3,content);
                            }
                        });
                    }

                    function openInfo(content, e) {
                        var p = e.target;
                        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
                        var infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象
                        map.openInfoWindow(infoWindow, point); //开启信息窗口
                    }
                }
            },
            error: function () {
                alert("地图显示异常,请刷新重试!");
            }

        });
    }

    // 刷新左一
    function refreshLeftOne(type) {
        $.ajax({
            url: '/dashuju/gettongji',
            type: 'post',
            data: {type: type},
            success: function (data) {
                if (data.flag == "1") {
                    if (data.g == "") {
                        // 只有综合页面显示6个区域
                        $("#left_gg").hide();
                    } else {
                        $("#left_gg").show();
                    }
                    var a = data.a.split(",");
                    var b = data.b.split(",");
                    var c = data.c.split(",");
                    var d = data.d.split(",");
                    var e = data.e.split(",");
                    var f = data.f.split(",");
                    var g = data.g.split(",");
                    $("#left_a").html(a[0]);
                    $("#left_b").html(b[0]);
                    $("#left_bv").html(b[1]);
                    $("#left_c").html(c[0]);
                    $("#left_cv").html(c[1]);
                    $("#left_d").html(d[0]);
                    $("#left_dv").html(d[1]);
                    $("#left_e").html(e[0]);
                    $("#left_ev").html(e[1]);
                    $("#left_f").html(f[0]);
                    $("#left_fv").html(f[1]);
                    $("#left_g").html(g[0]);
                    $("#left_gv").html(g[1]);
                }
            },
        });
    }

    // 刷新预警分析图
    function refreshYujingTu(type) {
        $.ajax({
            url: '/dashuju/getyujingtu',
            type: 'post',
            data: {type: type},
            success: function (data) {
                if (data.flag == "1") {
                    var a = data.a.split(",");
                    var b = data.b.split(",");
                    var c = data.c.split(",");
                    var d = data.d.split(",");
                    var e = data.e.split(",");
                    optionRecords = {
                        radar: {
                            indicator: [
                                {name: a[0], max: 5},
                                {name: b[0], max: 5},
                                {name: c[0], max: 5},
                                {name: d[0], max: 5},
                                {name: e[0], max: 5}
                            ],
                            name: {
                                textStyle: {
                                    color: '#5d9adf'
                                }
                            }
                        },
                        series: [{
                            name: '预警次数',
                            type: 'radar',
                            data: [
                                {
                                    value: [a[1], b[1], c[1], d[1], e[1]],
                                    name: '预警次数',
                                    label: {
                                        show: true,
                                        color: '#5d9adf',
                                        formatter: function (params) {
                                            return params.value;
                                        }
                                    }
                                }
                            ]
                        }]
                    };
                    //重新渲染统计图
                    yujingTu.setOption(optionRecords);
                }
            },
            error: function (e) {
                layer.alert("获取预警图失败！")
            },
        });
    }

    // 刷新工单概况分析图
    function refreshGongdanTu(type) {
        optionRecords = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                top: '5%',
                left: 'center',
                textStyle: {
                    color: '#5d9adf'//字体颜色
                }
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '15',
                            fontWeight: 'bold',
                            color: '#5d9adf'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: [
                        {value: 104, name: '管网工单'},
                        {value: 73, name: '水厂工单'},
                        {value: 58, name: '设备检修'},
                        {value: 48, name: '水源地工单'}
                    ]
                }
            ]
        };
        gongdanTu.setOption(optionRecords);
    }

    // 刷新监控数据
    function refreshJiankongTu(type) {
        optionRecords = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['供水完成率', '耗电完成率', '水费回收率', '产销差率', '水质合格率'],
                textStyle: {
                    color: '#5d9adf'//字体颜色
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['01-15', '02-15', '03-15', '04-15', '05-15', '06-15'],
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {
                        show: true,
                        interval: 'auto',
                        formatter: '{value} %',
                        textStyle: {
                            color: '#5d9adf'
                        }
                    },
                    show: true
                }
            ],
            series: [
                {
                    name: '供水完成率',
                    type: 'bar',
                    emphasis: {
                        focus: 'series'
                    },
                    data: [32, 33, 30, 33, 39, 33]
                },
                {
                    name: '耗电完成率',
                    type: 'bar',
                    stack: '广告',
                    emphasis: {
                        focus: 'series'
                    },
                    data: [12, 13, 10, 13, 9, 23]
                },
                {
                    name: '水费回收率',
                    type: 'bar',
                    stack: '广告',
                    emphasis: {
                        focus: 'series'
                    },
                    data: [22, 18, 19, 23, 29, 33]
                },
                {
                    name: '产销差率',
                    type: 'bar',
                    stack: '广告',
                    emphasis: {
                        focus: 'series'
                    },
                    data: [15, 23, 20, 15, 19, 33]
                },
                {
                    name: '水质合格率',
                    type: 'bar',
                    data: [86, 1, 96, 12, 69, 10],
                    emphasis: {
                        focus: 'series'
                    }
                }
            ]
        };
        jiankongTu.setOption(optionRecords);
    }

    function refreshGongshuiTu(type) {
        optionRecords = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['供水量（m³）'],
                x: 'right',
                textStyle: {
                    color: 'pink'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            yAxis: {
                name: '供水量（m³）',
                nameTextStyle: {
                    color: '#5d9adf'
                },
                type: 'value',
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            series: [
                {
                    name: '供水量（m³）',
                    type: 'line',
                    data: ['20000', '15000', '18000', '21000', '16000'],
                    color: 'pink'
                }
            ]
        };
        //重新渲染统计图
        gongshuiTu.setOption(optionRecords);
    }

    function refreshHandianTu(type) {
        optionRecords = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['耗电量（度）'],
                x: 'right',
                textStyle: {
                    color: 'rgb(234,159,58)'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            yAxis: {
                name: '耗电量（度）',
                nameTextStyle: {
                    color: '#5d9adf'
                },
                type: 'value',
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            series: [
                {
                    name: '耗电量（度）',
                    type: 'line',
                    data: ['1000', '1200', '1800', '2100', '1100'],
                    color: 'rgb(234,159,58)'
                }
            ]
        };
        //重新渲染统计图
        handianTu.setOption(optionRecords);
    }

    function refreshShuifeiTu(type) {
        optionRecords = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['水费（万元）'],
                x: 'right',
                textStyle: {
                    color: 'rgb(105,211,243)'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            yAxis: {
                name: '水费（万元）',
                nameTextStyle: {
                    color: '#5d9adf'
                },
                type: 'value',
                axisLabel: {
                    textStyle: {
                        color: '#5d9adf'
                    }
                }
            },
            series: [
                {
                    name: '水费（万元）',
                    type: 'line',
                    data: ['500', '300', '400', '200', '600'],
                    color: 'rgb(105,211,243)'
                }
            ]
        };
        //重新渲染统计图
        shuifeiTu.setOption(optionRecords);
    }

    // 右一图
    function refreshRightOne(type,name) {
        var type1Data1 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type1Data2 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type2Data1 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type2Data2 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type2Data3 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type3Data1 = [Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000)];
        var type3Data2 = [Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000)];
        if (type == 1) {
            optionRecords = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['流入', '流出'],
                    x: 'right',
                    textStyle: {
                        color: '#5d9adf'//字体颜色
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                },
                yAxis: [{
                    name: '',
                    nameTextStyle: {
                        color: '#5d9adf'
                    },
                    type: 'value',
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                }],
                series: [
                    {
                        name: '流入',
                        type: 'line',
                        data: type1Data1
                    },
                    {
                        name: '流出',
                        type: 'line',
                        data: type1Data2
                    }
                ]
            };
        } else if (type == 2) {
            optionRecords = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data: ['日供水量', '日耗电量', '产销差'],
                    textStyle: {
                        color: '#5d9adf'//字体颜色
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: ['01-15', '02-15', '03-15', '04-15', '05-15', '06-15'],
                        axisLabel: {
                            textStyle: {
                                color: '#5d9adf'
                            }
                        }
                    }
                ],
                yAxis: [
                    {
                        name: "",
                        nameTextStyle: {
                            color: '#5d9adf'
                        },
                        type: 'value',
                        axisLabel: {
                            show: true,
                            interval: 'auto',
                            textStyle: {
                                color: '#5d9adf'
                            }
                        },
                    }
                ],
                series: [
                    {
                        name: '日供水量',
                        type: 'bar',
                        barWidth: 10,
                        emphasis: {
                            focus: 'series'
                        },
                        data: type2Data1
                    },
                    {
                        name: '日耗电量',
                        type: 'bar',
                        stack: '广告',
                        barWidth: 10,
                        emphasis: {
                            focus: 'series'
                        },
                        data: type2Data2
                    },
                    {
                        name: '产销差',
                        type: 'bar',
                        barWidth: 10,
                        data: type2Data3,
                        emphasis: {
                            focus: 'series'
                        }
                    }
                ]
            };
        } else if (type == 3) {
            optionRecords = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data: ['日供水量', '日耗电量'],
                    textStyle: {
                        color: '#5d9adf'//字体颜色
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: ['01-15', '02-15', '03-15', '04-15', '05-15', '06-15'],
                        axisLabel: {
                            textStyle: {
                                color: '#5d9adf'
                            }
                        }
                    }
                ],
                yAxis: [
                    {
                        name: "",
                        nameTextStyle: {
                            color: '#5d9adf'
                        },
                        type: 'value',
                        axisLabel: {
                            show: true,
                            interval: 'auto',
                            textStyle: {
                                color: '#5d9adf'
                            }
                        },
                    }
                ],
                series: [
                    {
                        name: '日供水量',
                        type: 'bar',
                        barWidth: 10,
                        emphasis: {
                            focus: 'series'
                        },
                        data: type3Data1
                    },
                    {
                        name: '日耗电量',
                        type: 'bar',
                        stack: '广告',
                        barWidth: 10,
                        emphasis: {
                            focus: 'series'
                        },
                        data: type3Data2
                    }
                ]
            };

        }
        if(name){
            optionRecords.yAxis[0].name = name;
        }
        rightOneTu.setOption(optionRecords,true);
    }

    // 右二图
    function refreshRightTwo(type,name) {
        optionRecords = {
            series: [{
                type: 'gauge',
                axisLine: {
                    lineStyle: {
                        width: 5,
                        color: [
                            [0.3, '#67e0e3'],
                            [0.7, '#37a2da'],
                            [1, '#fd666d']
                        ]
                    }
                },
                pointer: {
                    itemStyle: {
                        color: 'auto',

                    },
                    length: 25,
                    width: 2
                },
                axisTick: {
                    distance: -2,
                    length: 4,
                    lineStyle: {
                        color: '#fff',
                        width: 2
                    }
                },
                splitLine: {
                    distance: -1,
                    length: 3,
                    lineStyle: {
                        color: '#fff',
                        width: 4
                    }
                },
                axisLabel: {
                    color: 'auto',
                    distance: 7,
                    fontSize: 7
                },
                detail: {
                    valueAnimation: true,
                    formatter: '{value} km/h',
                    color: 'auto',
                    fontSize: 7
                },
                data: [{
                    value: 70,
                    name: '神奇'
                }],
                title: {
                    textStyle: {
                        color: "#5d9adf"
                        , fontSize: "10"
                    }
                }
            }]
        };
        if(name){
            $("#rightName").html(name);
        }
        dynamic(type);
    }

    // 右三图
    function refreshRightThree(type,name) {
        var type1Data1 = [Math.floor(Math.random()*10),Math.floor(Math.random()*10),
            Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10),
            Math.floor(Math.random()*10),Math.floor(Math.random()*10)];
        var type2Data1 = [Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000)];
        var type2Data2 = [Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),Math.floor(Math.random()*1000),
            Math.floor(Math.random()*1000),Math.floor(Math.random()*1000)];
        var type3Data1 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type3Data2 = [Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100),Math.floor(Math.random()*100),
            Math.floor(Math.random()*100),Math.floor(Math.random()*100)];
        var type3Data3 = [Math.floor(Math.random()*1),Math.floor(Math.random()*1),
            Math.floor(Math.random()*1),Math.floor(Math.random()*1),Math.floor(Math.random()*1),
            Math.floor(Math.random()*1),Math.floor(Math.random()*1)];
        if (type == 1) {
            optionRecords = {
                xAxis: {
                    type: 'category',
                    data: ['01-15', '02-15', '03-15', '04-15'
                        , '05-15', '06-15', '07-15'],
                    boundaryGap: false,
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }

                },
                yAxis: {
                    name: '',
                    type: 'value',
                    nameTextStyle: {
                        color: '#5d9adf'
                    },
                    axisLabel: {
                        show: true,
                        interval: 'auto',
                        formatter: '{value} %',
                        textStyle: {
                            color: '#5d9adf'
                        }
                    },
                    show: true
                },
                series: [{
                    data: type1Data1,
                    type: 'bar',
                    barWidth: 10,
                    color: '#5d9adf',
                    showBackground: true,
                    backgroundStyle: {
                        color: 'rgba(180, 180, 180, 0.2)'
                    }
                }]
            };
        }else if(type == 2){
            optionRecords = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['日供水量', '日耗电量'],
                    x: 'right',
                    textStyle: {
                        color: '#5d9adf'//字体颜色
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                },
                yAxis: {
                    name:'',
                    nameTextStyle: {
                        color: '#5d9adf'
                    },
                    type: 'value',
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                },
                series: [
                    {
                        name: '日供水量',
                        type: 'line',
                        data: type2Data1
                    },
                    {
                        name: '日耗电量',
                        type: 'line',
                        data: type2Data2
                    }
                ]
            };
        }else if(type == 3){
            optionRecords = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['日供水量', '日耗电量', '产水率'],
                    x: 'right',
                    textStyle: {
                        color: '#5d9adf'//字体颜色
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['01-15', '02-15', '03-15', '04-15', '05-15'],
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                },
                yAxis: {
                    name:'',
                    nameTextStyle: {
                        color: '#5d9adf'
                    },
                    type: 'value',
                    axisLabel: {
                        textStyle: {
                            color: '#5d9adf'
                        }
                    }
                },
                series: [
                    {
                        name: '日供水量',
                        type: 'line',
                        data: type3Data1
                    },
                    {
                        name: '日耗电量',
                        type: 'line',
                        data: type3Data2
                    },
                    {
                        name: '产水率',
                        type: 'line',
                        data: type3Data3
                    }
                ]
            };
        }
        if(name){
            optionRecords.yAxis.name = name;
        }
        rightThreeTu.setOption(optionRecords,true);
    }

    // 右图二刷新方法
    function dynamic(type) {
        if(type == 1){
            optionRecords.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "流量";
            optionRecords.series[0].max = 100;
            optionRecords.series[0].detail.formatter = "{value} km/h";
            rightTwoTu.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "压力";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} mpa";
            rightTwoTu_1.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "流速";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} m³/s";
            rightTwoTu_2.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "流量";
            optionRecords.series[0].max = 100;
            optionRecords.series[0].detail.formatter = "{value} km/h";
            rightTwoTu_3.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "压力";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} mpa";
            rightTwoTu_4.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "流速";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} m³/s";
            rightTwoTu_5.setOption(optionRecords);
        }else if(type == 2){
            optionRecords.series[0].data[0].value = (Math.random() * 1).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "浑浊度";
            optionRecords.series[0].max = 1;
            optionRecords.series[0].detail.formatter = "{value} NTU";
            rightTwoTu.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "色度";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} 度";
            rightTwoTu_1.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 1).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "游离氯";
            optionRecords.series[0].max = 1;
            optionRecords.series[0].detail.formatter = "{value} mg/l";
            rightTwoTu_2.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "菌落总数";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} CFU/ml";
            rightTwoTu_3.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "总大肠杆菌数";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} CFU/100ml";
            rightTwoTu_4.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "PH";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} ";
            rightTwoTu_5.setOption(optionRecords);
        }else if(type == 3){
            optionRecords.series[0].data[0].value = (Math.random() * 1).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "浑浊度";
            optionRecords.series[0].max = 1;
            optionRecords.series[0].detail.formatter = "{value} NTU";
            rightTwoTu.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "色度";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} 度";
            rightTwoTu_1.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "菌落总数";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} CFU/ml";
            rightTwoTu_2.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "总大肠杆菌数";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} CFU/100ml";
            rightTwoTu_3.setOption(optionRecords);
            optionRecords.series[0].data[0].value = (Math.random() * 10).toFixed(2) - 0;
            optionRecords.series[0].data[0].name = "PH";
            optionRecords.series[0].max = 10;
            optionRecords.series[0].detail.formatter = "{value} ";
            rightTwoTu_4.setOption(optionRecords);
            rightTwoTu_5.setOption({},true);
        }
    };

    refreshDiTu("0");
    refreshLeftOne("0");
    refreshYujingTu("0");
    refreshGongdanTu();
    refreshJiankongTu();
    refreshGongshuiTu();
    refreshHandianTu();
    refreshShuifeiTu();

    // 监听切换tab页
    $(".bottom-menu").on('click', function () {
        $(".active").removeClass("active");
        $(this).addClass("active");
        var type = $(this).children().children().text();
        if (type == '综合') {
            type = 0;
            $("#rightOne,#rightTwo,#rightThree").css("z-index", "-1");
            $("#rightZonghe").show();
            refreshJiankongTu();
            refreshGongshuiTu();
            refreshHandianTu();
            refreshShuifeiTu();
        } else if (type == '管网') {
            type = 1;
            $("#rightZonghe").hide();
            $("#rightOneTitle").html("管道流量分析");
            $("#rightTwoTitle").html("管道指标");
            $("#rightThreeTitle").html("产销率");
            $("#rightOne,#rightTwo,#rightThree").css("z-index", "1");
            refreshRightOne(type);
            refreshRightTwo(type);
            refreshRightThree(type);
        } else if (type == '水厂') {
            type = 2;
            $("#rightZonghe").hide();
            $("#rightOneTitle").html("水厂工况");
            $("#rightTwoTitle").html("水厂水质指标");
            $("#rightThreeTitle").html("水厂动态");
            $("#rightOne,#rightTwo,#rightThree").css("z-index", "1");
            refreshRightOne(type);
            refreshRightTwo(type);
            refreshRightThree(type);
        } else if (type == '水源') {
            type = 3;
            $("#rightZonghe").hide();
            $("#rightOneTitle").html("水源地工况");
            $("#rightTwoTitle").html("水源地水质指标");
            $("#rightThreeTitle").html("水源地动态");
            $("#rightOne,#rightTwo,#rightThree").css("z-index", "1");
            refreshRightOne(type);
            refreshRightTwo(type);
            refreshRightThree(type);
        }
        refreshDiTu(type);
        refreshLeftOne(type);
        refreshYujingTu(type);
        refreshGongdanTu();
        $("#rightName").html("");
    });

    // 返回首页
    $(".el-icon-s-home").on('click', function () {
        var url =encodeURI('../index.html?username='+un);
        window.location = url;
    });

})