$(function(){
	$('.create-button').click(function(){
		var url = document.location.pathname + "url/",
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
			$(kl).html('<p>This link is already exist in database. Please enter another link.</p>');	
		} else {
			$(kl).html('<p>Your long URL:<br><a style="color:#9b9b9b;" href="'+resp.longURL+'">'+resp.longURL + '</a></p>' + 
						'<p>Your short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
						'<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button></p>');
		}
		
		$(kl).append('<button class="close-button" onclick="$(\''+kl+'\').slideUp()">Close</button>');
		
		$(kl).slideDown();
	};
	
	
	
	
	
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
			$(kl).html('<p>This short link does not exist in database. Please enter another link.</p>');	
		} else {
			$(kl).html('<p>Long URL:<br><a style="color:#9b9b9b;" href="'+resp.longURL+'">'+resp.longURL + '</a></p>' + 
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
		
		$(kl).append('<button class="close-button" onclick="$(\''+kl+'\').slideUp()">Close</button>');
		
		$(kl).slideDown();
		
		$('.tag-button').click(function(){
			var tag = $(this).text(), url = document.location.pathname + 'tags/' + tag;
			$.ajax({
				type: 'GET',
				url: url,
				dataType: 'json',
				async: true,
				success: function(result){
					$('.view-tag-result').html('<hr><hr><p>Tag: '+tag+'</p>');
					ViewUrlByTag(result)
				},
				error: function(jqXHR, textStatus, errorThrown){
	                alert(jqXHR.status + ' ' + jqXHR.responseText);
	            }
			})
		});
		
		function ViewUrlByTag(resp) {
			
			$('.view-tag-result').append('<hr>');
			var i, len = resp.length;
			for(i=0;i<len;i++){
				var url = resp[i], shortURL = document.baseURI + url.shortURL;
				
				$('.view-tag-result').append('<p>Long URL:<br><a style="color:#9b9b9b;" href="'+url.longURL+'">'+url.longURL + '</a></p>' + 
						'<p>Short URL:<br><a style="color:#E32934" href="'+shortURL+'">'+shortURL+'</a>'+
						'<button class="copy-button" data-clipboard-text="'+shortURL+'">Copy</button></p><hr>');
			}
		}
	};
	
	
	
	
	
	
	$('.header-create-button').click(function(){
		$('.view-url').hide();
		$('.create-url').show();
	});
	
	$('.header-view-button').click(function(){
		$('.create-url').hide();
		$('.view-url').show();
	});
	
	
	
	
	
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
			
			
			var url = document.location.pathname + "user/" + user,
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
					if (result.message === "OK"){
						$('.login-menu').html('<br><h2>Congratulations !!!<br>You successfully login</h2>'+
											  '<br><button class="close-button">Close</button><br>');
						$('.close-button').click(function() {
							$('.login-container').remove();
							$('.header-menu-right .header-userName').text('User name: '+ user);
							$('.header-menu-left').css('display','block');
						});
					} else {
						$(kl).html(result.message);
						return;;
					}
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
					if (result.message === "OK"){
						$('.login-menu').html('<br><h2>Congratulations !!!<br>You become a user of our service</h2>'+
											  '<br><button class="close-button">Close</button><br>');
						$('.close-button').click(function() {
							$('.login-container').remove();
							$('.header-menu-right .header-userName').text('User name: '+ user);
							$('.header-menu-left').css('display','block');
						});
					} else {
						$(kl).html(result.message);
						return;;
					}
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
			
});
