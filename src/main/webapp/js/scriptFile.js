/**
 * 
 */

$(document).ready(function() {
	$("#editbtn").hide();
	$("#bak").hide();
	$("#confirm").hide();
	$("#second").hide();
	$("#third").hide();
			$.ajax({
				type : "GET",
				url : "http://localhost:8080/OCB_JSP/rs/ocb/getDetails/890",
				contentType : "application/json;charset=utf-8",
				dataType : "json",

				success : function(res) {
					var jsonObj = res.list; // storing result in json object
					$(jsonObj).each(function() {
								var row = $(this)[0]; // extracting row obj
														// one by one
								var skuNum = row["skuNumber"];
								var skuDesc = row["skuDescription"];
								var ordQty = row["orderQty"];
								$(
										'<tr class="row"><td>' + skuNum + '</td><td>'
												+ skuDesc + '</td><td class="edit">'
												+ ordQty + '</td></tr>')
										.appendTo('#skulist');

							});
				},
				error : function(err) {
					alert(err);
				}

			});//end of ajax get function
			
});
			
			//edit table column values
			$(function () { 
				$(document).on('dblclick','.edit',function(){					
					var OriginalContent = $(this).text(); 
					$(this).addClass("cellEditing"); 
					$(this).html("<input type='text' value='" + OriginalContent + "' />"); 
					$(this).children().first().focus(); 
					$(this).children().first().select(); 
					$(this).children().first().keypress(function (e) {
					if (e.which == 13) {
						var newContent = $(this).val();
						
							if(newContent != ""){
						$(this).parent().text(newContent); 
						$(this).parent().removeClass("cellEditing"); 
						}
						}
						});
						
						$(this).children().first().blur(function(){ 
						$(this).parent().text(OriginalContent);
						$(this).parent().removeClass("cellEditing");
						}); 
				}); 
	
			
				
			//grab the edited value in the quantity column on submit button click
			
			$(document).on('click','#submit',function(){
			var tableData;
				tableData = storeTblvalues();		
				function storeTblvalues(){
					var tableData = new Array();
				//loop through the table rows
				$('#skulist tbody tr').each(function(row,tr){
					tableData[row] = {
							"skuNumber" :parseInt($(tr).find('td:eq(0)').text()),
							"skuDescription" : $(tr).find('td:eq(1)').text(),
							"orderQty" : parseInt($(tr).find('td:eq(2)').text())						
					}
					
				});
				return tableData;
				}
				console.log("Value : "+tableData)
				var List = JSON.stringify(tableData);
				console.log(List);
			
				  //ajax call to post the html table values
				  $.ajax({
						type : "POST",
						url : "http://localhost:8080/OCB_JSP/rs/ocb/updateOrderDetails/",
						contentType : "application/json;charset=utf-8",
						data : {orderList:List},
						dataType:"json",
						success : function(res){
							console.log("success :");
							$("#skulist tbody tr.row").hide();
							
							$("#first").hide();
							$("#second").show();
							$("#third").hide();
				
							console.log("result"+res);
							var jsonObj = res.list;
							sessionStorage.setItem('jsonObj', jsonObj);
							$(jsonObj).each(function() {
								var row = $(this)[0]; // extracting row obj
														// one by one
								var skuNum = row["skuNumber"];
								var skuDesc = row["skuDescription"];
								var ordQty = row["orderQty"];
								$(
										'<tr class="admin"><td>' + skuNum + '</td><td>'
												+ skuDesc + '</td><td class="edit">'
												+ ordQty + '</td></tr>')
												.appendTo('#skulist tbody');
								

							});
							
							
							$("#editbtn").show();
							$("#bak").show();
							$("#confirm").show();
							$("#submit").hide();
													
						},
						error : function() {
							alert("Error in updation");
						}
				  }); //end of ajax POST
			  
			  });
			  
});//end of button click function
			
			//On Clicking back button
			
			$(document).on('click', '#bak', function() {
			
				$("#skulist tbody tr.admin").empty();
				$("#skulist tbody tr.row").show();
				$("#submit").show();
				$("#confirm").hide();
			
			});

	//if 'keep original quantities' button is clicked		
			$(document).on('click', '#editbtn', function() {
				$("#confirm").hide();
				var arr=[];
				$.ajax({
					type : "GET",
					url : "http://localhost:8080/OCB_JSP/rs/ocb/getOrgQty/",
					contentType : "application/json;charset=utf-8",
					dataType : "json",

					success : function(res) {
						console.log("inside org qty get"+res);
						var obj =  [];
						var jsonObj = res.list; // storing result in json object
						$(jsonObj).each(function() {
									var row = $(this)[0];
									var ordQty = row["orderQty"];
									obj.push(ordQty);
									
						});
						console.log(obj);	
						for (i =0;i<=obj.length;i++){
							$('#skulist tbody tr.admin td.edit').eq(i).html(obj[i]);
						
						};
					},
					error : function(err) {
						alert(err);
					}

				});// end of ajax call
				

			});
			
			$(document).on('click', '#confirm', function() {
				
				$("#skulist").empty();
				
				$("#first").hide();
				$("#second").hide();
				$("#third").show();
				$.ajax({
					type : "GET",
					url : "http://localhost:8080/OCB_JSP/rs/ocb/getPONumber/",
					contentType : "application/json;charset=utf-8",
					dataType : "json",

					success : function(res) {
						console.log(res);
						$("#res").append("PO Number : " + res["string"]);

					},
					error : function(err) {
						alert(err);
					}
				});
				
				$("#editbtn").hide();
				$("#bak").hide();
				$("#submit").hide();
				$("#confirm").hide();
				var closeBtn = $('<input type="button" id="close" class="btn" value="Close" onclick="self.close()"/>');
		        $("body #btns").append(closeBtn);
			
				
			});
			$(document).on('click', '#close', function() {
				
				window.close();
			});
			
