atsaControllers.controller('VerAfiliadoController', ['$scope','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams',
    function ($scope, $rootScope, AfiliadosService, $modal, $translate, $stateParams) {
		$scope.afiliadoId = $stateParams.afiliadoId;
		$scope.afiliado = {};
		$scope.estudios = [];
		$scope.familiares = [];
		$scope.tramites = [];
		
		$scope.init = function() {
			AfiliadosService.getAfiliado($scope.afiliadoId).
				get().$promise.then(	function(response) {
						if (response.afiliado.tipoPersona == 'FAMILIAR') {
							AfiliadosService.findAfiliadoByFamiliarId($scope.afiliadoId).get().$promise.then(
									function(response2) {
										AfiliadosService.getAfiliado(response2.id).
											get().$promise.then(	function(response3) {
												$scope.afiliado = response3.afiliado;
												$scope.estudios = response3.estudios;
												$scope.familiares = response3.familiares;
												$scope.tramites = response3.tramites;
												$scope.notas = '';
											}
										);
									}, 
									function(response) {
										$rootScope.showError(response.status.toString());
									}
								);
						} else {
							$scope.afiliado = response.afiliado;
							$scope.estudios = response.estudios;
							$scope.familiares = response.familiares;
							$scope.tramites = response.tramites;
							$scope.notas = '';
						}
					}, 
					function(response) {
						$rootScope.showError(response.status.toString());
					});
		}
	}
]);
