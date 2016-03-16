
function queryEncode(params)
{
	var str = '';
	params.each(function(key,value,index){
        str +=key+"="+(encodeURIComponent(value))+"&";
    });


	 return str.substring ( 0, str.length-1);
}

function getRequestJson(params)
{
	var str = '{';
	params.each(function(key,value,index){
	    if(key!='geotable_id')
		{
		   str+="'"+key+"'"+":"+"'"+encodeURIComponent(value)+"'"+",";
		}
	     else{
		    		   str+="'"+key+"'"+":"+encodeURIComponent(value)+",";
 
		 }
        
    });

	str= str.substring ( 0, str.length-1)+"}";
	return str;
	
}


function caculateAKSN(sk, url, querystring_arrays) {
    
    var querystring = queryEncode(querystring_arrays);
	
	 var temp=encodeURIComponent(url+"?"+querystring+sk);
	 
	
    
	return $.md5(temp);
}
function getjson(url,data)
{
           
	       var data1 = queryEncode(data);
	       if(data1!="")
	       var url1 =url+ '?' +data1;
		   alert(url1);
		   
		   data.remove("callback");
		   
		   var jsonp=getRequestJson(data);
		   alert("jsonp:"+jsonp); 
		   $.getJSON(url+"?callback=?",jsonp ,function(e) {
		     alert(e);
           
        }); 
		
		$.ajax({
		    type:"get",
			url:url1,
			dataType:"json",
			success:function(data){
			$.each(data.contents,function(i,item){
			       alert(item.location[0]+item.location[1]);
			   });
			
			}
			});
		   
		   
		   $("#ss").text(url1);
	       $.getJSON(url1, function(e){
	    	   alert("Data Loaded: " + e);
			   
			   $.each(e.contents,function(i,item){
			       alert(item.location[0]+item.location[1]);
			   });
			   alert(e.contents);
	    	 });
	       
}
function searchLocal(inputArr,tableid,ak,sk) {
	var baseUrl="http://api.map.baidu.com";
	//������¼�ĵ�ַ
	var spUrl = "/geosearch/v3/local";
	//ͨ�ò���
	var paras = new Map();
	
	paras.put("callback","?");
	paras.put("geotable_id", tableid);
	paras.put("ak",ak);
	paras.put("q","");
 
	
	
	//������Ĳ���д���ϴ�����
	inputArr.each(function(key,value,index){
        paras.put(key, value);
    });
 
	var sn = caculateAKSN(sk , spUrl , paras );
	paras.put("sn", sn);
	alert(sn);
	getjson(baseUrl+spUrl , paras);
}









function test()
{
	var data = new Map();
	data.put("region", "长沙市");
	
	
    searchLocal(data,"123182","GTHzlUeOj8GotfIr1NZ4VTqt",
    		 "UvcvrwRoRRMOx9FjGotGNMcTU2ZWwYym"); 

}


