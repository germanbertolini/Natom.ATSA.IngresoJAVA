atsaControllers.controller('MainController', ['$scope','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams', '$state',
    function ($scope, $rootScope, AfiliadosService, $modal, $translate, $stateParams, $state) {
		$scope.aprobadosTramites = [];
		$scope.aprobadosDocumentos = [];
		$scope.rechazadosTramites = [];
		$scope.rechazadosDocumentos = [];
		$scope.porAprobar = [];
		$scope.porAprobarDocumentos = [];
		
		$scope.init = function() {
			AfiliadosService.findTramitesAprobadosTramites().
				query().$promise.then(	function(response) {
					$scope.aprobadosTramites = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});
			AfiliadosService.findTramitesAprobadosDocumentos().
				query().$promise.then(	function(response) {
					$scope.aprobadosDocumentos = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});

			AfiliadosService.findTramitesRechazadosTramites().
				query().$promise.then(	function(response) {
					$scope.rechazadosTramites = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});

			AfiliadosService.findTramitesRechazadosDocumentos().
				query().$promise.then(	function(response) {
					$scope.rechazadosDocumentos = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});

			AfiliadosService.findTramitesPorAprobar().
				query().$promise.then(	function(response) {
					$scope.porAprobar = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});

			AfiliadosService.findTramitesPorAprobarDocumentos().
				query().$promise.then(	function(response) {
					//console.debug(response);
					$scope.porAprobarDocumentos = response;
				}, 
				function(response) {
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
				});
		}
	}
]);

