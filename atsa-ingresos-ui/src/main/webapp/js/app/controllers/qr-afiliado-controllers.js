atsaControllers.controller('QrAfiliadoController', ['$scope','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams', '$state',
    function ($scope, $rootScope, AfiliadosService, $modal, $translate, $stateParams, $state) {
	
		$scope.start = function() {
			$('#reader').html5_qrcode($scope.readQR,
			    function(error){
			        console.debug(error);
			    }, function(videoError){
			    	console.debug(videoError);
			        //the video stream could be opened
			    }
			);
		}
		
		$scope.qrError = "";
		$scope.readQR = function(data) {
			$scope.qrError = "";
			var version = data.substring(data.lastIndexOf("/")+1);
			var idPart = data.substring(0, data.lastIndexOf("/"));
			var id = idPart.substring(idPart.lastIndexOf("/")+1);
			if (id && version) {
				AfiliadosService.getAfiliado(id).
					get().$promise.then(function(response) {
							if (!response || !response.afiliado.id) {
								$scope.qrError = "No se encuentra el afiliado. Carnet inválido.";
							} else if (response.afiliado.versionCarnet == version) {
								window.location.href= "/atsa-ingresos-ui-new/#!/ver-afiliado/" + id;
							} else {
								$scope.qrError = "Carnet expirado";
							}
						}, 
						function(response) {
							$rootScope.showError(response.status.toString());
							$scope.stopSpin();
						});
			}
		}
	}
]);
