$(function(){
	
	function CreateUrl(user) {
		$('.main-container').html('<div class="create-menu">'+
										'<label>URL to be shorten<br>'+
								  			'<input type="text" class="inputLongURL" name="longURL" value="https://www.owasp.org/index.php/REST_Security_Cheat_Sheet" autocomplete="off">'+
							  			'</label><br>'+
							  			'<label>Description<br>'+
								  			'<textarea class="inputDescription" name="description" rows="4">This is another link to resource</textarea>'+
							  			'</label><br>'+
							  			'<label>Tags (separate by comma)<br>'+
								  			'<input type="text" class="inputTags" name="tags" value="spring, NoSQL,   mongoDB" autocomplete="off">'+
							  			'</label><br><br>'+
							  			'<button class="create-button big-button">Shorten</button>'+
									'</div>'+
									'<div class="create-result"></div>');
		
		$('.create-button').click(function(){
			var url = document.location.pathname + "url/",
				JSONobj = {
					userName: user,
					longURL: $('.inputLongURL').val(),
					description: $('.inputDescription').val(),
					tags:$('.inputTags').val().split(",").map(function(text){return text.trim()})
				};
			$.ajax({
				type: 'POST',
				url: url,
				dataType: 'json',
				data: JSON.stringify(JSONobj),
				contentType:'application/json',
				async: true,
				success: function(result){
					ViewShortUrl(result)
				},
				error: function(jqXHR, textStatus, errorThrown){
	                alert(jqXHR.status + ' ' + jqXHR.responseText);
	            }
			})		
		});
		
		function ViewShortUrl(resp) {
		    var shortURL = document.baseURI + resp.shortURL,
		    	kl = ".create-result";
			
			if (resp.shortURL == null){
				$(kl).html('<hr><p>This link is already exist in database. Please enter another link.</p>');	
			} else {
				$(kl).html('<hr><p>Your long URL:<br><a style="color:#9b9b9b;" href="'+resp.longURL+'">'+resp.longURL + '</a></p>' + 
							'<p>Your short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
							'<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button></p>');
			}
			
			$(kl).append('<button class="close-button">Close</button>');
			$(kl).append('<script> new Clipboard(".copy-button");</script>');
			
			$('.close-button').click(function() {
				$(kl).slideUp(1000, function() {
					$(kl).empty();
				});
			});
			
			$(kl).slideDown();
		};
	};
	
	function ViewUrl() {
		$('.main-container').html('<div class="view-menu">'+
										'<label>Enter short URL to viewing full info<br>'+
								  			'<input type="text" class="inputShortURL" name="ShortURL" value="http://localhost:8080/ShortURLCreator/Af5w6K" autocomplete="off">'+
							  			'</label><br><br>'+
							  			'<button class="view-button big-button">View info</button>'+
									'</div>'+
									'<div class="view-result"></div>');
									
		$('.view-button').click(function(){
			var userUrl = $('.inputShortURL').val();
			if(document.location.href === userUrl.slice(0,-6)){
				var url = document.location.pathname + 'url/' + userUrl.replace(document.location.href,"");
				$.ajax({
					type: 'GET',
					url: url,
					dataType: 'json',
					async: true,
					success: function(result){
						ViewUrlInfo(result)
					},
					error: function(jqXHR, textStatus, errorThrown){
						alert(jqXHR.status + ' ' + jqXHR.responseText);
		            }
				})
			}
			else {
				alert('Incorrect URL!!! Please check the input.');
			}
		});
		
		function ViewUrlInfo(resp) {
		    var shortURL = document.baseURI + resp.shortURL,
		    	kl = ".view-result";
			
			if (resp.shortURL == null){
				$(kl).html('<hr><p>This short link does not exist in database. Please enter another link.</p>');	
			} else {
				$(kl).html('<hr><p>Long URL:<br><a style="color:#9b9b9b;" href="'+resp.longURL+'">'+resp.longURL + '</a></p>' + 
							'<p>Short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a></p>' +
							'<p>Description:<br>'+resp.description+'</p>');
				
				var i, len = resp.tags.length, text='<p>Tags:<br>';
				for(i=0;i<len;i++){
					var tag = resp.tags[i];
					if (tag !== ""){
						text += '<button class="tag-button">' + tag + '</button>';
					}
				}
				$(kl).append(text+'</p><div class="view-tag-result"></div>');
			}
			
			$(kl).append('<button class="close-button">Close</button>');
			$(kl).append('<script> new Clipboard(".copy-button");</script>');
			
			$('.close-button').click(function() {
				$(kl).slideUp(1000, function() {
					$(kl).empty();
				});
			});
			
			$(kl).slideDown(1000);
			
			$('.tag-button').click(function(){
				var tag = $(this).text(), url = document.location.pathname + 'tags/' + tag;
				$.ajax({
					type: 'GET',
					url: url,
					dataType: 'json',
					async: true,
					success: function(result){
						ViewUrlByTag(result, tag)
					},
					error: function(jqXHR, textStatus, errorThrown){
		                alert(jqXHR.status + ' ' + jqXHR.responseText);
		            }
				})
			});
			
			function ViewUrlByTag(resp, tg) {
				
				$('.view-tag-result').html('<hr><hr><p>Tag: '+tg+'</p><hr>');
				var i, len = resp.length;
				for(i=0;i<len;i++){
					var url = resp[i], shortURL = document.baseURI + url.shortURL;
					$('.view-tag-result').append('<p>Long URL:<br><a style="color:#9b9b9b;" href="'+url.longURL+'">'+url.longURL + '</a></p>' + 
												'<p>Short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
												'<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button></p><hr>');
				};
				
				$('.view-tag-result').slideDown(1000);
			}
		};
	};
	
	
	
	ViewUrl();
	
	$('.header-login-button').click(function() {
		LoginMenu("Login");
		var kl = '.warning';
		$('.submit-button').click(function() {
			if($(kl).length !== 0){
				$(kl).empty();
			}
			var user = $('.login-username').val(),
				pass = $('.login-password').val();
			
			if( user === ""){
				$(kl).html('User name must not be empty !!!');
				return;
			};
			if( pass === ""){
				$(kl).html('Password must not be empty !!!');
				return;
			};
			if( pass.length < 6){
				$(kl).html('Password length too short<br>(must be 6 or more chars)!!!');
				return;
			};
			
			
			var url = document.location.pathname + "login/",
				JSONobj = {
					userName: user,
					password: pass
				};
			$.ajax({
				type: 'POST',
				url: url,
//				username:user,
//				password:pass,
				dataType: 'json',
				data: JSON.stringify(JSONobj),
				contentType:'application/json',
				async: true,
				success: function(result){
					SuccessLogin(result, 'You successfully login', user);
				},
				error: function(jqXHR, textStatus, errorThrown){
	                alert(jqXHR.status + ' ' + jqXHR.responseText);
	            }
			})
			
		});

	});
	
	$('.header-signup-button').click(function() {
		LoginMenu("Sign up");
		var kl = '.warning';
		$(kl).before('<p><label>Confirm password'+
					'<br><input type="password" class="confirm-password" autocomplete="off" required>'+
					'</label></p>');
		$('.submit-button').click(function() {
			if($(kl).length !== 0){
				$(kl).empty();
			}
			var user = $('.login-username').val(),
				pass = $('.login-password').val(),
				passConfirm = $('.confirm-password').val();
			
			if( user === ""){
				$(kl).html('User name must not be empty !!!');
				return;
			};
			if( pass === ""){
				$(kl).html('Password must not be empty !!!');
				return;
			};
			if( pass.length < 6){
				$(kl).html('Password length too short<br>(must be 6 or more chars)!!!');
				return;
			};
			if( pass !== passConfirm){
				$(kl).html('Please input correct confirm password !!!');
				return;
			};
			
			
			var url = document.location.pathname + "user/",
				JSONobj = {
					userName: user,
					password: pass
				};
			$.ajax({
				type: 'POST',
				url: url,
				dataType: 'json',
				data: JSON.stringify(JSONobj),
				contentType:'application/json',
				async: true,
				success: function(result){
					SuccessLogin(result, 'You become a user of our service', user);
				},
				error: function(jqXHR, textStatus, errorThrown){
	                alert(jqXHR.status + ' ' + jqXHR.responseText);
	            }
			})
			
		});
	});
			
	function LoginMenu(title) {
		$('body').append('<div class="login-container">'+
							'<div class="login-menu">'+
								'<br><h3>'+title+'</h3><hr>'+
								'<button class="close-menu tag-button">X</button>'+
								'<p><label>User name'+
									'<br><input type="text" class="login-username" autocomplete="off" required>'+
								'</label></p>'+
								'<p><label>Password'+
									'<br><input type="password" class="login-password" autocomplete="off" required>'+
								'</label></p>'+
								'<p class="warning"></p>' +
								'<button class="submit-button">Submit</button><br>'+
							'</div>'+
						 '</div>');
		$('.close-menu').click(function() {
			$('.login-container').remove();
		});
		
	};
	
	function SuccessLogin(res, mes, user) {
		if (res.message === "OK"){
			$('.login-menu').html('<br><h2>Congratulations !!!<br>'+ mes +'</h2>'+
								  '<br><button class="close-button">Close</button><br>');
			$('.close-button').click(function() {
				$('.login-container').remove();
				$('.header-menu-right .header-userName').text('User name: '+ user);
				$('.header-menu-left').html('<ul>'+
												'<li><button class="header-create-button">Create shortURL</button></li><li>'+
												'<button class="header-view-button">View others shortURL</button></li><li>'+
												'<button class="header-viewAllMy-button">View all my shortURL</button></li>'+
											'</ul>');
				$('.header-create-button').click(function(){
					CreateUrl(user);
				});
				
				$('.header-view-button').click(function(){
					ViewUrl();
				});
				
				$('.header-viewAllMy-button').click(function(){
					ViewAllMyUrl(user);
				});
				
			});
		} else {
			$('.warning').html(res.message);
		}
	}
			
});
