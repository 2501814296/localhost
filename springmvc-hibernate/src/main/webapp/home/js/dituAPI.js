// <script type="text/javascript" src="http://api.map.baidu.com/api?ak=yourkey&v=2.0&services=false"></script>
var map = new BMap.Map("container");          // 创建地图实例
var point = new BMap.Point(120.391655,36.067588);  // 创建点坐标
map.centerAndZoom(point, 15);
//map.centerAndZoom("北京", 15);
//map.centerAndZoom("Hongkong", 15);

map.addControl(new BMap.NavigationControl());//缩放平移控件
map.addControl(new BMap.ScaleControl());    //比例尺
map.addControl(new BMap.OverviewMapControl());//缩略图
map.addControl(new BMap.MapTypeControl()); //地图类型
map.setCurrentCity("青岛");

map.addEventListener("click", function(){
    alert("您点击了地图。");
});
map.removeEventListener("click", functionA);
map.addEventListener("click", functionA);

var point = new BMap.Point(120.389472,36.072362);//默认  可以通过Icon类来指定自定义图标
var marker = new BMap.Marker(point);
var label = new BMap.Label("青岛市政府",{offset:new BMap.Size(20,-10)});//标注标签
marker.setLabel(label)//设置标注说明
marker.enableDragging();//标注可以拖动的
marker.addEventListener("dragend", function(e){
    alert(e.point.lng + ", " + e.point.lat);//打印拖动结束坐标
});
map.addOverlay(marker);

var point = new BMap.Point(120.387244,36.064835);
var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25));
var marker2 = new BMap.Marker(point, {icon: myIcon});
map.addOverlay(marker2);
var infoWindow = new BMap.InfoWindow("<p style='font-size:14px;'>详细信息</p>");  //弹出窗口
marker2.addEventListener("click", function(){
    this.openInfoWindow(infoWindow);
});


var search = new BMap.LocalSearch("中国", {
    onSearchComplete: function(result){
        if (search.getStatus() == BMAP_STATUS_SUCCESS){
            var res = result.getPoi(0);
            var point = res.point;
            map.centerAndZoom(point, 9);
        }
    },renderOptions: {  //结果呈现设置，
        map: map,
        autoViewport: true,
        selectFirstResult: true
    } ,onInfoHtmlSet:function(poi,html){
        // alert(html.innerHTML)
    }
});

















var x = "117.00";
var y = "36.40";
var lev1 = "11";

var map;
map = new BMap.Map("container");    //创建地图
map.centerAndZoom(new BMap.Point(x,y), lev1);//初始化地图(坐标洛阳市)
map.addControl(new BMap.MapTypeControl());//添加地图类型控件
//map.setMapType(BMAP_HYBRID_MAP);//此地图类型展示卫星和路网的混合视图
map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
map.enableScrollWheelZoom();//启用地图滚轮放大缩小
map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
map.enableKeyboard();//启用键盘上下左右键移动地图

//地图显示位置
var city = new BMap.LocalSearch(map,{renderOptions:{map:map,autoViewport:true}});   //地图显示到查询结果处
var s = $("#wz").val();
city.search(s);   //查找城市


var dwname=$('#sydwmc').val();
$.ajax({
    url:"getMap",
    type:"post",
    data:{"dwname":dwname},
    dataType:"json",
    success:function(result){
        console.log(result);
        var myIcon = new BMap.Icon("${pageContext.request.contextPath }/static/img/38375162504f550ebe57afd1912b4b3.png",new BMap.Size(60,80));
        var linePoints = [];
        for(var i=0;i<result.length;i++){ //循环赋值
            //显示标注
            var pt = new BMap.Point(result[i].x, result[i].y);
            var marker = new BMap.Marker(pt,{icon:myIcon});        // 创建标注
            map.addOverlay(marker);
            //marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            //linePoints.push(pt);
            //添加连线
            //var polyline = new BMap.Polyline(linePoints, {strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});   //创建折线
            //map.addOverlay(polyline);   //增加折线

            //设置文本标题显示
            var label = new BMap.Label(result[i].dw,{offset:new BMap.Size(20,-10)});
            marker.setLabel(label);
            //设置点击事件信息窗口  按照官方你文档这样写的，不然就出错
            var opts = {
                position : pt,    // 指定文本标注所在的地理位置
                width : 100,     // 信息窗口宽度
                height: 50,     // 信息窗口高度
                offset   : new BMap.Size(20, -20), //设置文本偏移量
                title : "详情" , // 信息窗口标题
            }
            var info_html=result[i].dw;
            addClickHandler(info_html,marker);

        };


        function addClickHandler(content,marker){
            marker.addEventListener("click",function(e){
                openInfo(content,e)}
            );
        }
        function openInfo(content,e){
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
            var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow,point); //开启信息窗口
        }

    },
    error:function(){
        alert("地图显示异常,请刷新重试!");
    }

});

//跳转地图信息
// function setPoint(name){
//     var search = new BMap.LocalSearch("中国", {
//     onSearchComplete: function(result){
//             if (search.getStatus() == BMAP_STATUS_SUCCESS){
//                 var res = result.getPoi(0);
//                 var point = res.point;
//                 map.centerAndZoom(point, 9);
//             }
//         }
//         ,renderOptions: {  //结果呈现设置，
//             map: map,
//             autoViewport: true,
//             selectFirstResult: true
//         } ,onInfoHtmlSet:function(poi,html){
//             // alert(html.innerHTML)
//         }
//     });
//     search.search(name);
// }


