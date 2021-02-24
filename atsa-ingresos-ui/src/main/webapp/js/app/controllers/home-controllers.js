/**
 * Home Controller
 */
atsaControllers.controller('HomeController', [ '$scope', 'usSpinnerService', '$rootScope', 'HomeService',
		'$state', function($scope, usSpinnerService, $rootScope, HomeService, $state) {

			$scope.user = $rootScope.user;
			$scope.parameters = {};
			
			$scope.startSpin = function(){
		        usSpinnerService.spin('spinner-1');
		    }
		    $scope.stopSpin = function(){
		        usSpinnerService.stop('spinner-1');
		    }
		    
		    $scope.logOut = function () {
		    	$rootScope.logout();
		    }

			/* Comportamiento Menu y Nav Bar */
			$scope.isCollapsed = "";
			$scope.indexSelected = "";
			$scope.isHidden = false;
			$scope.classContent = 'main_content_show_nav';
			$scope.classNavBar = 'nav_bar_show';
			$scope.classHeader = 'header_row_show';
			$scope.classFooter = 'footer_show';

			$scope.menuShowHide = function(index) {
				if ($scope.indexSelected == index) {
					$scope.indexSelected = "";
					$scope.isCollapsed = "";
				} else {
					$scope.indexSelected = index;
					$scope.isCollapsed = index;
				}
			};

			$scope.navBarShowHide = function() {
				$scope.isHidden = !$scope.isHidden;
				if ($scope.isHidden) {
					$scope.classContent = 'main_content_hide_nav';
					$scope.classNavBar = 'nav_bar_hide';
					$scope.classHeader = 'header_row_hide';
					$scope.classFooter = 'footer_hide';
				} else {
					$scope.classContent = 'main_content_show_nav';
					$scope.classNavBar = 'nav_bar_show';
					$scope.classHeader = 'header_row_show';
					$scope.classFooter = 'footer_show';
				}
			}
			
			/**
			 * Se cargan todos los parámetros (donde se llene un combo)
			 */
			$scope.loadApplicationParamenters = loadApplicationParamenters;
			
			function loadApplicationParamenters() {
				// TIPOS BAJA (tiposBaja)
				if ($scope.user != null && !$scope.parameters.tiposBaja) {
					HomeService.getAllTiposBaja().
						query().$promise.then(
							function(response) {
								$scope.parameters.tiposBaja = response;
							}, 
							function(response) {
								$rootScope.showError(response.status.toString());
							}
						);
				}
				
				// CONVENIOS (convenios)
				if ($scope.user != null && !$scope.parameters.convenios) {
					HomeService.getAllConvenios().
						query().$promise.then(	function(response) {
													$scope.parameters.convenios = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
				
				// ESTADOS (estados)
				if ($scope.user != null && !$scope.parameters.estados) {
					HomeService.getAllEstados().
						query().$promise.then(	function(response) {
													$scope.parameters.estados = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
		        
				// PAISES (paises)
				if ($scope.user != null && !$scope.parameters.paises) {
					HomeService.getAllPaises().
						query().$promise.then(	function(response) {
													$scope.parameters.paises = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
		        
				// ROLES (roles)
				if ($scope.user != null && !$scope.parameters.roles) {
					HomeService.getAllRoles().
						query().$promise.then(	function(response) {
													$scope.parameters.roles = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
		        
				// DOCUMENTOS ARCHIVADOS (tipoDocumentosArchivados)
				if ($scope.user != null && !$scope.parameters.tipoDocumentosArchivados) {
					HomeService.getAllTipoDocumentosArchivados().
						query().$promise.then(	function(response) {
													$scope.parameters.tipoDocumentosArchivados = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
		        
				// TIPOS DE RELACIONES (tiposRelaciones)
				if ($scope.user != null && !$scope.parameters.tiposRelaciones) {
					HomeService.getAllTipoRelaciones().
						query().$promise.then(	function(response) {
													$scope.parameters.tiposRelaciones = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
		        
				// TIPOS DE TRAMITES (tiposTramites)
				if ($scope.user != null && !$scope.parameters.tiposTramites) {
					HomeService.getAllTipoTramites().
						query().$promise.then(	function(response) {
													$scope.parameters.tiposTramites = response;
												}, 
												function(response) {
													$rootScope.showError(response.status.toString());
												});
				}
			}

			init();

			/**
			 * Inicializa el controller.
			 */
			function init() {
				loadApplicationParamenters();
			}

			/**
			 * Determina si el role pasado como parametro es el activo
			 * 
			 * @param aRole
			 * @returns {boolean}
			 */
			function isActiveRole(aRole) {
				return $rootScope.user.roles[aRole];
			}

			$scope.isActiveRole = isActiveRole;

			$scope.userHasPermission = function(aRole) {
				return $rootScope.user.roles[aRole];
			}

			$scope.nuevoInforme = function() { // EJEMPLO
				$state.go('home.regInforme');
			}

			$scope.home = function() {
				$state.go('home.main');
			}

			$scope.busquedaAfiliados = function() {
				$state.go('home.buscar-afiliados');
			}
			
			$scope.editarAfiliado = function(afiliado) {
				if (afiliado && afiliado.tipoPersona == 'FAMILIAR') {
					HomeService.findAfiliadoByFamiliarId(afiliado.id).get().$promise.then(
						function(response) {
							$state.go('home.editar-afiliado', {"afiliadoId": response.id});
						}, 
						function(response) {
							$rootScope.showError(response.status.toString());
						}
					);
				} else {
					if (afiliado && afiliado.id) {
						$state.go('home.editar-afiliado', {"afiliadoId": afiliado.id});
					} else if (afiliado) {
						$state.go('home.editar-afiliado', {"afiliadoId": afiliado});
					} else {
						$state.go('home.editar-afiliado', {"afiliadoId": ''});
					}
				}
			}
			
			$scope.administrarEstablecimientos = function(afiliadoId) {
				$state.go('home.administraro-establecimientos');
			}
			
			$scope.verMain = function() {
				$state.go('home.main');
			}
		} ]);







var DatepickerDemoCtrl = function ($scope) {
	
  $scope.minDate = new Date(2010, 1, 1);
  $scope.today = function() {
    $scope.dt = new Date();
  };
  $scope.today();

  $scope.showWeeks = false;
  $scope.toggleWeeks = function () {
    $scope.showWeeks = ! $scope.showWeeks;
  };

  $scope.clear = function () {
    $scope.dt = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
  };

  $scope.toggleMin = function() {
    $scope.minDate = ( $scope.minDate ) ? null : new Date();
  };
  $scope.toggleMin();

  $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    'format-year': "'yyyy'",
	'format-day': "'dd'",
	'format-month': "'MM'",
	'starting-day': 1
  };

  $scope.formats = ['dd/MM/yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];


};