atsaControllers.controller('EstablecimientosController', ['$scope','$rootScope', 'EstablecimientosService', '$modal','$translate', '$stateParams',
    function ($scope, $rootScope, EstablecimientosService, $modal, $translate, $stateParams) {
		$scope.establecimientos = [];
		
		$scope.page = 0;
		$scope.nombre = '';
		$scope.cuil = '';
	
		$scope.init = function() {
			$scope.establecimientos = [];
			$scope.page = 0;
			$scope.findEstablecimientos();
		}
		
		$scope.nextPage = function () {
			$scope.page = $scope.page + 1;
			$scope.findEstablecimientos();
		}
		
		$scope.findEstablecimientos = function() {
			EstablecimientosService.findAll($scope.page, $scope.nombre, $scope.cuil).
				query().$promise.then(	function(response) {
						if (response.length) {
							for (var i = 0; i < response.length; i++) {
								$scope.establecimientos.push(response[i]);
							}
						} else {
							$scope.page = -1;
						}
					}, 
					function(response) {
						$rootScope.showError(response.status.toString());
					});
		}
		
		$scope.getEstablecimiento = function(id) {
			EstablecimientosService.findById(id).
				get().$promise.then(function(response) {
						$scope.establecimiento = response;
					}, 
					function(response) {
						$rootScope.showError(response.status.toString());
					});
		}
		
		$scope.editarEstablecimiento = function (establecimiento){
			var parameters = $scope.parameters;
			var ModalInstanceCtrl = function ($scope, $modalInstance) {
                $scope.establecimiento = (establecimiento) ? angular.copy(establecimiento) : {};
                $scope.clear = (establecimiento) ? true : false;
                $scope.parameters = parameters;
                $scope.close = function (nuevoEstablecimiento) {
                    $modalInstance.close(nuevoEstablecimiento);
                };
            };

			var modalInstance = $modal.open({
            	  backdrop:'static',
            	  keyboard: false,
	              templateUrl: 'partials/establecimiento.html',
	              controller: ModalInstanceCtrl,
	              resolve: {
	                  
	              }
            });

            modalInstance.result.then(function (nuevoEstablecimiento) {
                if(nuevoEstablecimiento != null){
                	$scope.guardarEstablecimiento(nuevoEstablecimiento);
                }}, function () {});
		}
		
		$scope.guardarEstablecimiento = function(nuevoEstablecimiento) {
			EstablecimientosService.saveEstablecimiento().saveEstablecimiento({}, angular.toJson(nuevoEstablecimiento), function(response) {
				$scope.establecimientos = [];
				$scope.findEstablecimientos();
				
				$rootScope.showInfo("La información se ha guardado con éxito");
            });
		}
		
		$scope.eliminarEstablecimiento = function(establecimiento) {
			EstablecimientosService.eliminarEstablecimiento(establecimiento.id).
				get().$promise.then(function(response) {
						$scope.findEstablecimientos();
						
						$rootScope.showInfo("La información se ha guardado con éxito");
					}, 
					function(response) {
						$rootScope.showError(response.status.toString());
					});
		}
	}
]);



atsaControllers.controller('EstablecimientoController', ['$scope','$rootScope', 'EstablecimientosService', '$modal','$translate', '$stateParams',
    function ($scope, $rootScope, EstablecimientosService, $modal, $translate, $stateParams) {
		$scope.guardar = function() {
			$scope.$parent.$parent.close($scope.establecimiento);
		}
	}
]);


