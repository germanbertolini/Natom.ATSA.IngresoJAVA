'use strict';


/**
 * CONSTANTES DE LA APP
 */
var MODALES_BACKDROP = 'static';
var MODALES_KEYBOARD = false;

/**
 * App Definition with modules dependencies.
 * @type {module|*}
 */
var atsaApp = angular.module('atsaApp', [
        'ngRoute',
        'ngCookies',
        'textAngular',
        'atsaControllers',
        'atsaServices',
        'atsaDirectives',
        'pascalprecht.translate',
        'ui.router',
        'ui.bootstrap',
        'informesFilters',
        'chieffancypants.loadingBar',
        'ngAnimate',
        'angularSpinner',
        'flow',
        'autocomplete',
        'omr.directives'
]);

/**
 * Configura la loading-bar
 */
atsaApp.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
  });


/**
 *  Configura la varialble de Services 
 */
var baseUrl = '/atsa-ingresos-core-new/rest/';
var atsaServices = angular.module('atsaServices', ['ngResource']);

/**
 *  Configura la varialble de Controllers 
 */
var atsaControllers = angular.module('atsaControllers', ['atsaServices']);


/**
 * App routing config.
 */
atsaApp.config([ '$locationProvider', '$httpProvider', '$translateProvider',
        '$stateProvider', '$urlRouterProvider',
        function($locationProvider, $httpProvider, $translateProvider,
                 $stateProvider, $urlRouterProvider) {


            // For any unmatched url, redirect to /home/main by default.
            // if the user is logged, is the main page
            $urlRouterProvider.otherwise("/login");
            
            //
            // Set up the states
            $stateProvider
                .state('login', {
                    url: "/login",
                    templateUrl: 'partials/login.html',
                    controller: 'LoginController',
                    access: {
                        isFree: true
                    }
                })
                .state('ver-afiliado', {
                	url: "/ver-afiliado/:afiliadoId",
                	templateUrl: "partials/ver-afiliado.html",
                	controller: 'VerAfiliadoController',
                    access: {
                        isFree: true
                    }
                })
                .state('qr-afiliado', {
                	url: "/qr-afiliado",
                	templateUrl: "partials/qr-afiliado.html",
                	controller: 'QrAfiliadoController',
                    access: {
                        isFree: true
                    }
                })
                //State base de la aplicacion una vez logueado. El home solo no puede ser
                //instanciado, por eso abstract. Tiene que tener un contenido como subState.
                .state('home', {
                    abstract: true,
                    url: "/home",
                    templateUrl: "partials/home.html",
                    controller: 'HomeController'
                })
                .state('home.main', {
                	url: "/main",
                	templateUrl: "partials/home.main.html",
                	controller: 'MainController',
                    access: {
                        isFree: false
                    }
                })
                .state('home.buscar-afiliados', {
                	url: "/afiliados",
                	templateUrl: "partials/home.buscar-afiliados.html",
                	controller: 'AfiliadosController',
                    access: {
                        isFree: false
                    }
                })
                .state('home.editar-afiliado', {
                	url: "/afiliado/:afiliadoId",
                	templateUrl: "partials/home.editar-afiliado.html",
                	controller: 'AfiliadosController',
                    access: {
                        isFree: false
                    }
                })
                .state('home.administraro-establecimientos', {
                	url: "/establecimientos",
                	templateUrl: "partials/home.editar-establecimientos.html",
                	controller: 'EstablecimientosController',
                    access: {
                        isFree: false
                    }
                })
            $locationProvider.hashPrefix('!');

            /* Intercept http errors */
            var interceptor = function ($rootScope, $q, $location) {
                function success(response) {
                    return response;
                }

                //TODO Manejar el error 500 aca tb.

                function error(response) {

                    var status = response.status;
                    var config = response.config;
                    var method = config.method;
                    var url = config.url;

                    if (status == 401) {
                        $rootScope.error = 'INVALID_SESSION';
                        $state.go('login');
                    }

                    return $q.reject(response);
                }

                return function (promise) {
                    return promise.then(success, error);
                };
            };
            $httpProvider.responseInterceptors.push(interceptor);

            /*Translate and i18n config */
            $translateProvider.useStaticFilesLoader({
                prefix: 'lang/',
                suffix: '.json'
            });

            //Definición del lenguaje por default:
            $translateProvider.preferredLanguage('es');

            //si fuera browser based usar:
            /*$translateProvider.determinePreferredLanguage();*/

            //si fuera dependiente de una opción del usuario usar:
            /*$translateProvider.determinePreferredLanguage(function () {
                // define a function to determine the language
                // and return a language key
                Obtener acá el usuario seleccionado por el usuario y setearlo con
                $translateProvider.preferredLanguage('es');
            });*/

            //catchea los archivos de lenguaje en el local storage.
            //$translateProvider.useLocalStorage();
            
            
         // Use x-www-form-urlencoded Content-Type
            $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
            $httpProvider.defaults.headers.put['Content-Type'] = 'application/json';
            
            /**
             * The workhorse; converts an object to x-www-form-urlencoded serialization.
             * @param {Object} obj
             * @return {String}
             */ 
            var param = function(obj) {
              var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
                
              for(name in obj) {
                value = obj[name];
                  
                if(value instanceof Array) {
                  for(i=0; i<value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                  }
                }
                else if(value instanceof Object) {
                  for(subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                  }
                }
                else if(value !== undefined && value !== null)
                  query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
              }
                
              return query.length ? query.substr(0, query.length - 1) : query;
            };
           
            // Override $http service's default transformRequest
            $httpProvider.defaults.transformRequest = [function(data) {
              return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
            }];


        } ]

    ).run(function($rootScope, $http, $location,$modal,$translate, $cookieStore, $state, LoginService) {

    	/**
		 * Define el funciones para conversión de fechas UTC a angular dataFilter
		 */
		$rootScope.toUTCDate = function(date){
		    var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
		    return _utc;
		};
		
		/**
		 * Define el funciones para conversión de fechas UTC a angular dataFilter
		 */
		$rootScope.millisToUTCDate = function(millis){
		    return toUTCDate(new Date(millis));
		};
		
		/**
		 * Define el ShowError para toda la aplicacion
		 */
		$rootScope.showError = function(i18n){
		    var ModalInstanceCtrl = function($scope, $modalInstance) {
		        $scope.close = function() {
		          $modalInstance.close();
		        };
		    };
		    $rootScope.errorMessage = $translate.instant(i18n);
		    if ($rootScope.errorMessage == "")	$rootScope.errorMessage = $translate.instant('common.show_error.mensaje_default');
		    
	        var modalInstance = $modal.open({
	          backdrop : MODALES_BACKDROP,
			  keyboard: MODALES_KEYBOARD,
			  templateUrl: "partials/dialog-modal-error.html",
	          controller: ModalInstanceCtrl,
	          windowClass: 'dialog-modal'
	        });
	    };
	    
	    /**
		 * Define el ShowInfo para toda la aplicacion
		 */
		$rootScope.showInfo = function(i18n){
		    var ModalInstanceCtrl = function($scope, $modalInstance) {
		        $scope.close = function() {
		          $modalInstance.close();
		        };
		    };
		    
		    $rootScope.infoMessage = $translate.instant(i18n);
		    if ($rootScope.infoMessage == "") $rootScope.infoMessage = $translate.instant('common.show_info.mensaje_default');
		    
	        var modalInstance = $modal.open({
	          backdrop : MODALES_BACKDROP,
		      keyboard: MODALES_KEYBOARD,
		      templateUrl: "partials/dialog-modal-info.html",
	          controller: ModalInstanceCtrl,
	          windowClass: 'dialog-modal'
	        });
	    };

	    /**
		 * Define el ShowConfirm para toda la aplicacion
		 */
        $rootScope.showConfirm = function(scope, i18n){
		    var ModalInstanceCtrl = function($rootScope, $modalInstance) {
			  $rootScope.ok = function() {
			    $modalInstance.close();
			  };
			
			  $rootScope.cancel = function() {
			    $modalInstance.dismiss('cancel');
			  };
		    };
		    
		    $rootScope.confirmMessage = $translate.instant(i18n);
		    if ($rootScope.confirmMessage == "") $rootScope.confirmMessage = $translate.instant('common.show_confirm.mensaje_default');
		    
	        var modalInstance = $modal.open({
	          backdrop : MODALES_BACKDROP,
		      keyboard: MODALES_KEYBOARD,
		      templateUrl: "partials/dialog-modal-confirm.html",
	          controller: ModalInstanceCtrl,
	          windowClass: 'dialog-modal'
	        });
	        
	        modalInstance.result.then(function() {
	        	scope.confirmedClick();
	        }, function() {
	        	//Modal dismissed
	        });
	    };
    	
        /* Reset error when a new view is loaded */
        $rootScope.$on('$viewContentLoaded', function() {
            delete $rootScope.error;
        });

        $rootScope.hasRole = function(role) {

            if ($rootScope.user === undefined) {
                return false;
            }

            if ($rootScope.user.roles[role] === undefined) {
                return false;
            }

            return $rootScope.user.roles[role];
        };

        //defaults application background color
        $rootScope.defaultBackgroundColor = 'green';

        $rootScope.backgroundColor = $rootScope.defaultBackgroundColor;

        $rootScope.logout = function() {
            delete $rootScope.user;
            delete $http.defaults.headers.common['X-Auth-Token'];
            $cookieStore.remove('user');
            $cookieStore.remove('selectedProfile');
            $cookieStore.remove('userProfiles');
            $cookieStore.remove('JSESSIONID');
            $rootScope.backgroundColor = $rootScope.defaultBackgroundColor;
            $state.go("login");
        };

        /* Try getting valid user from cookie or go to login page */
        var originalPath = $location.path();
        $rootScope.originalPath = originalPath;
       
        var user = $cookieStore.get('user');
        if (user !== undefined) {
            $rootScope.user = user;
            $rootScope.selectedProfile = $cookieStore.get("selectedProfile");
            $rootScope.userProfiles = $cookieStore.get("userProfiles");
            $http.defaults.headers.common['X-Auth-Token'] = user.token;

            $location.path(originalPath);
        }
        
        //manejo de cambios de pagina. Solo puede ir a los estados protegidos si esta logeado.
        //esto evita que pueda usar el history o el back button del browser luego de deslogueado
        $rootScope.$on('$routeChangeSuccess', function(scope, currView, prevView) {
            if (!currView.access.isFree && !$rootScope.user) {
                $location.path("/login");
            }
        });

        //manejo de cambios de pagina. Solo puede ir a los estados protegidos si esta logeado.
        //esto evita que pueda usar el history o el back button del browser luego de deslogueado
        $rootScope.$on("$stateChangeStart",
            function (event, toState, toParams, fromState, fromParams) {
                if (!toState.access.isFree && !$rootScope.user) {
                    event.preventDefault();
                    $state.go('login');
                }
            });

    });