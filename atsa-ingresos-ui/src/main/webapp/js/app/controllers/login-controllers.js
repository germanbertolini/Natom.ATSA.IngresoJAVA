/**
 * Login Controller
 */
atsaControllers.controller('LoginController', ['$location', '$scope', '$rootScope', '$state', '$http', '$cookieStore', 'LoginService','$modal','$translate',
    function($location, $scope, $rootScope, $state, $http, $cookieStore, LoginService, $modal, $translate) {
	
		//completar esto para testear mas rapido :)
        $scope.username = "";
        $scope.password = '';
        $scope.selectedProfile = '';
        
        $scope.error = "";
        
        $scope.seleccionarPerfil = false;
        
        $scope.login = function() {
            var userlogin = {username: $scope.username, password: $scope.password};
            if (validate(userlogin)) {
                LoginService.login(userlogin).authenticate(function(response) {
                	if (response.status == 'OK') {
                        var user = response.userData;
                        $rootScope.user = user;
                        $http.defaults.headers.common['X-Auth-Token'] = user.token;
                        $cookieStore.put('user', user);
                        
                        if ($rootScope.originalPath && $rootScope.originalPath != '/login') {
                        	$location.path($rootScope.originalPath);
                        } else {
                        	$state.go("home.main");
                        }
                        
                    } else {
                    	$scope.error = "Usuario y/o contraseña incorrectos";
                    }
                });
            }
        };

        function validate(userlogin) {
            if (!userlogin.username || !userlogin.password){
                $rootScope.error = "USERNAME_AND_PASSWORD_NOT_BLANK";
                return false;
            } else {
                return true;
            }
        }
    }
]);