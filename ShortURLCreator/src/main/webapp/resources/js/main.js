$(function(){
	
//	--------------------------------------------------- Create short URL ------------------------------------------------------
	function CreateUrl() {
		$('.main-container').html('<div class="create-menu">'+
										'<label>URL to be shorten<br>'+
								  			'<input type="text" class="inputLongURL" name="longURL" autocomplete="off">'+
							  			'</label><br>'+
							  			'<label>Description<br>'+
							  			'<textarea class="inputDescription" name="description" rows="4"></textarea>'+
								  			'</label><br>'+
							  			'<label>Tags (separate by comma)<br>'+
								  			'<input type="text" class="inputTags" name="tags" autocomplete="off">'+
							  			'</label><br><br>'+
							  			'<button class="create-button big-button">Shorten</button>'+
									'</div>'+
									'<div class="create-result"></div>');
		
		$('.create-button').click(function(){
			var url = document.location.pathname + "security/urls/",
				JSONobj = {
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
				headers:{'Authorization':'Bearer ' + sessionStorage.getItem('access_token')},
				async: true,
				success: function(result){
					ViewShortUrl(result)
				},
				error: function(jqXHR, textStatus, errorThrown){
					jqXHR.status == '409' ? Message('This link is already exist in database.<br> Please enter another link.') :
					(jqXHR.status == '403' ? Message('Access to this resource is forbidden. Please login correctly.') :
													alert(jqXHR.status + ' - ' + errorThrown));
	            }
			})		
		});
		
		function ViewShortUrl(resp) {
		    var shortURL = document.baseURI + resp.shortURL,
		    	kl = ".create-result";
			
			$(kl).html('<hr><p>Your long URL:<br><a style="color:#9b9b9b;" href="'+resp.longURL+'">'+resp.longURL + '</a></p>')
				 .append('<p>Your short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
						 '<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button></p>')
				 .append('<button class="close-button">Close</button>')
				 .append('<script> new Clipboard(".copy-button");</script>');
			
			$('.close-button').click(function() {
				$(kl).slideUp(1000, function() {
					$(kl).empty();
				});
			});
			
			$(kl).slideDown();
		};
	};
	
//	--------------------------------------------------- View short URL information ---------------------------------------------
	function ViewUrl() {
		$('.main-container').html('<div class="view-menu">'+
										'<label>Enter short URL to viewing full info<br>'+
								  			'<input type="text" class="inputShortURL" name="ShortURL" autocomplete="off">'+
							  			'</label><br><br>'+
							  			'<button class="view-button big-button">View info</button>'+
								  '</div>'+
								  '<div class="view-result"></div>');
									
		$('.view-button').click(function(){
			var userUrl = $('.inputShortURL').val();
			if(document.location.href === userUrl.slice(0,-6)){
				var url = document.location.pathname + 'urls/' + userUrl.replace(document.location.href,"");
				$.ajax({
					type: 'GET',
					url: url,
					dataType: 'json',
					async: true,
					success: function(result){
						ViewUrlInfo(result)
					},
					error: function(jqXHR, textStatus, errorThrown){
						jqXHR.status == '404' ? Message('This short link does not exist in database.<br> Please enter another link.') :
												alert(jqXHR.status + ' - ' + errorThrown);
					}
				})
			}
			else {
				Message('Incorrect URL!!!<br> Please check the input.');
			}
		});
		
		function ViewUrlInfo(resp) {
		    var shortURL = document.baseURI + resp.shortURL,
		    	kl = ".view-result";
			
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
			$(kl).append(text+'</p><div class="view-tag-result"></div>')
				 .append('<button class="close-button">Close</button>')
				 .append('<script> new Clipboard(".copy-button");</script>');
			
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
						jqXHR.status == '404' ? Message('This tag does not exist in database.<br> Please enter another link.') :
												alert(jqXHR.status + ' - ' + errorThrown);
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
	
//	--------------------------------------------------- View information of all user short URL ---------------------------------------------
	function ViewAllMyUrl(user) {

		var url = document.location.pathname + 'security/urls/user/' + user;
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			async: true,
			headers:{'Authorization':'Bearer ' + sessionStorage.getItem('access_token')},
			success: function(result){
				ViewAllUrlInfo(result)
			},
			error: function(jqXHR, textStatus, errorThrown){
				jqXHR.status == '404' ? Message('This user doesn\'t have any short link.') :
				(jqXHR.status == '403' ? Message('Access to this resource is forbidden. Please login correctly.') :
											alert(jqXHR.status + ' - ' + errorThrown));
			}
		})
		
		function ViewAllUrlInfo(resp) {
			$('.main-container').html('<div class="view-all-result"></div>');
			
			var kl = ".view-all-result";
			
			$(kl).html('<script> new Clipboard(".copy-button");</script>');
			
			var i, len = resp.length;
			for(i=0;i<len;i++){
				var url = resp[i], shortURL = document.baseURI + url.shortURL;
				$(kl).append('<p>Long URL:<br><a style="color:#9b9b9b;" href="'+url.longURL+'">'+url.longURL + '</a></p>' + 
							'<p class="clearfix">Short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
							'<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button>'+
							'<span class="redirect">redirect: '+url.redirectCount+'</span></p>'+
							'<div data-description="'+url.description+'">Description:<br>'+url.description+'</div><br>'+
							'<div><div style="display:inline-block; width:470px" data-tags="'+url.tags+'">Tags:<br>'+url.tags+'</div>'+
							'<button class="close-button edit" data-shorturl="'+url.shortURL+'">Edit</button></div><hr>');
			};
			$('.edit').click(function(){
				var shortURL = $(this).attr('data-shorturl'),
					tags = $(this).prev().attr('data-tags'),
					description = $(this).parent().prev().prev().attr('data-description');
				$('body').append('<div class="edit-container">'+
									'<div class="edit-menu">'+
										'<br><br><button class="close-menu tag-button">X</button>'+
										'<label>Description<br>'+
							  			'<textarea class="inputDescription" rows="4">'+description+'</textarea>'+
								  			'</label><br><br>'+
							  			'<label>Tags (separate by comma)<br>'+
								  			'<input type="text" class="inputTags" value="'+tags+'" autocomplete="off">'+
							  			'</label><br><br>'+
										'<button class="save-button">Save</button><br>'+
									'</div>'+
								 '</div>');
				$('.close-menu').click(function() {
					$('.edit-container').remove();
				});
				$('.save-button').click(function(){
					var url = document.location.pathname + "security/urls/" + shortURL,
						JSONobj = {
							description: $('.inputDescription').val(),
							tags:$('.inputTags').val().split(",").map(function(text){return text.trim()})
						};
					$.ajax({
						type: 'PUT',
						url: url,
						dataType: 'json',
						data: JSON.stringify(JSONobj),
						contentType:'application/json',
						headers:{'Authorization':'Bearer ' + sessionStorage.getItem('access_token')},
						async: true,
						success: function(result){
							$('.edit-container').remove();
							ViewAllMyUrl(user);
						},
						error: function(jqXHR, textStatus, errorThrown){
							jqXHR.status == '403' ? Message('Access to this resource is forbidden. Please login correctly.') :
															alert(jqXHR.status + ' - ' + errorThrown);
			            }
					})		
				});
			});
		};
	};
	  
	ViewUrl();

	function Message (message) {
		$('body').append('<div class="message-container">'+
							'<div class="message-menu">'+
								'<br><h3>'+message+'</h3>'+
								'<br><button class="close-button">Close</button><br>'+
							'</div>'+
					 	 '</div>');
		$('.close-button').click(function() {
			$('.message-container').remove();
		});
	}
	
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
	
//	--------------------------------------------------- Login user ------------------------------------------------------
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
				dataType: 'json',
				data: JSON.stringify(JSONobj),
				contentType:'application/json',
				async: true,
				success: function(result,textStatus,jqXHR){
					var token = result.access_token;
					if (token) {
						sessionStorage.setItem('access_token',token);
					}
					SuccessLogin(jqXHR, 'You successfully login', user);
				},
				error: function(jqXHR, textStatus, errorThrown){
					$(kl).html('Invalid user name or password.');
	            }
			})
			
		});

	});

//	--------------------------------------------------- Sign up user ------------------------------------------------------
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
			
			
			var url = document.location.pathname + "users/",
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
				success: function(result,textStatus,jqXHR){
					var token = result.access_token;
					if (token) {
						sessionStorage.setItem('access_token',token);
					}
					SuccessLogin(jqXHR, 'You become a user of our service.', user);
				},
				error: function(jqXHR, textStatus, errorThrown){
					if (jqXHR.status == "406"){
						$(kl).html('This user name is already taken.');
					}
					if (jqXHR.status == "400"){
						$(kl).html('Invalid user name or password.');
					}
	            }
			})
			
		});
	});
			
	function SuccessLogin(jqXHR, mes, user) {
		if (jqXHR.status == "200" || jqXHR.status == "201"){
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
				CreateUrl();
				$('.header-create-button').click(function(){
					CreateUrl();
				});
				
				$('.header-view-button').click(function(){
					ViewUrl();
				});
				
				$('.header-viewAllMy-button').click(function(){
					ViewAllMyUrl(user);
				});
				
			});
		} 
	}
			
});
