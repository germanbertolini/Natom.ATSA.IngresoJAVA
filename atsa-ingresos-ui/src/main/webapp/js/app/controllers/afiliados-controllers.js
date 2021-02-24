atsaControllers.controller('AfiliadosController', ['$scope','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams', '$state',
    function ($scope, $rootScope, AfiliadosService, $modal, $translate, $stateParams, $state) {
		$scope.administrable = "coso administrable";
		$scope.uploader = {};
		$scope.foto;
		$scope.tomandoFoto = false;
		$scope.stamp = 0;

		$scope.showFichaDesafiliacion = false;
		
		$scope.localidades = [];
		
		$scope.localidadSetted = function() {
			AfiliadosService.findLocalidadByNombre($scope.afiliado.localidad).
				get().$promise.then(function(response) {
						if (response.codigoPostal) $scope.afiliado.codigoPostal = response.codigoPostal;
						$scope.stopSpin();
					}, 
					function(response) {
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
						$scope.stopSpin();
					});
		}
		
		$scope.afiliados = [];
		
		$scope.afiliadoId = $stateParams.afiliadoId;
		
		$scope.afiliado = {
				profesion: 'Empleado'
		};
		$scope.familiar = {};
		$scope.estudios = [];
		$scope.familiares = [];
		$scope.documentos = [];
		$scope.tramites = [];
		$scope.establecimientos = [];
		$scope.modoEdicion = $scope.afiliadoId == "" ? true : false;

		$scope.tNotas = [];
		$scope.tCambios = [];
		$scope.tDocumentos = [];
		$scope.tNotasNextPage = 0;
		$scope.tCambiosNextPage = 0;
		$scope.tDocumentosNextPage = 0;
		
		$scope.metodoBusquedaSeleccionado = "manual";
		$scope.busquedaVisible = false;
		$scope.seccion = "afiliado";
		
		$scope.tipoDato = "numero-afiliado";
		
		$scope.start = function() {
			$('#reader').html5_qrcode($scope.readQR,
			    function(error){
			        //console.debug(error);
			    }, function(videoError){
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
								$state.go('home.editar-afiliado', {"afiliadoId": id});
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
		
		$scope.fotoTomada = function(media) {
			$scope.tomandoFoto = false;
		}
		
		$scope.tomarFoto = function() {
			if ($scope.modoEdicion) {
				$scope.tomandoFoto=true;
			}
		}
		
		$scope.fileUploadQuery = function (flowFile, flowChunk, isTest) {
			return {'tipoDocumento': flowFile.tipoDocumento.codigo, 'afiliadoId': $scope.afiliado.id};
		}
		
		$scope.validateFile = function($file) {
			//console.debug($file.size);
			if ($file.size > (2 * 1024 * 1024)) {
				$rootScope.showError('El archivo es demasiado grande');
				return false;
			}
			return true;
		}
		
		$scope.upload = function(newId) {
			$scope.startSpin();
			var done = false;
			$scope.uploader.flow.on('complete', function() {
				for (var i = 0; i < $scope.uploader.flow.files.length; i++) {
					if ($scope.uploader.flow.files[i].isComplete())
						$scope.uploader.flow.removeFile($scope.uploader.flow.files[i]);
				}
				if ($scope.uploader.flow.files.length  > 0) {
					$scope.uploader.flow.upload();
				} else if (!done) {
					done = true;
					$scope.editar();
					$scope.stopSpin();
					$rootScope.showInfo("La información se ha guardado con éxito");
					if (newId) $state.go('home.editar-afiliado', {"afiliadoId": newId});
				}
			});
			$scope.uploader.flow.on('error', function(message) {
				if (response.status == 500) {
					$state.go('login');
				} else {
					$rootScope.showError('Error de upload: ' + message);
				}
				$scope.stopSpin();
			});
			

			$scope.uploader.flow.upload();
		}
		
		$scope.removeFileToUpload = function(file) {
			$scope.uploader.flow.removeFile(file);
		}
		
		$scope.eliminarDocumento = function(documento) {
			var index = $scope.documentos.indexOf(documento);
			$scope.documentos.splice(index, 1);
		}
		
		$scope.debug = function(obj) {
			if (console) console.debug(obj);
		}
		
		$scope.sub2 = function(str) {
			return str.substring(0, 2);
		}
		
		$scope.findEstablecimientos = function (nombre, cuit) {
			$scope.establecimientos = [];
			$scope.startSpin();
			if (!nombre) nombre = '';
			nombre = nombre + '%';
			AfiliadosService.findEstablecimientos(nombre, cuit).
				query().$promise.then(	function(response) {
						$scope.establecimientos = response;
						$scope.stopSpin();
					}, 
					function(response) {
						$scope.stopSpin();
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
					});
		}
		
		$scope.selectEstablecimiento = function (establecimiento) {
			$scope.afiliado.establecimiento = establecimiento;
			$scope.establecimientos = [];
			$('#establecimientoModal').modal('hide');
		}
		
		$scope.rereshAllTramites = function() {
			$scope.tNotas = [];
			$scope.tNotasNextPage = 0;
			$scope.getTramiteNotas(0);
			$scope.tCambios = [];
			$scope.tCambiosNextPage = 0;
			$scope.getTramiteCambios(0);
			$scope.tDocumentos = [];
			$scope.tDocumentosNextPage = 0;
			$scope.getTramiteDocumentos(0);
		};
		
		$scope.getTramiteNotas = function(page) {
			AfiliadosService.findTramitesPageByTipo('Notas', $scope.afiliadoId, page).
				query().$promise.then(	function(response) {
											for (var i = 0; i < response.length; i++) {
												$scope.tNotas.push(response[i]);
											}
											if (response.length) $scope.tNotasNextPage = page + 1;
											else $scope.tNotasNextPage = -1;
										}, 
										function(response) {
											if (response.status == 500) {
												$state.go('login');
											} else {
												$rootScope.showError(response.status.toString());
											}
										});
		}
		
		$scope.getTramiteCambios = function(page) {
			$scope.startSpin();
			AfiliadosService.findTramitesPageByTipo('Cambios', $scope.afiliadoId, page).
				query().$promise.then(	function(response) {
						for (var i = 0; i < response.length; i++) {
							$scope.tCambios.push(response[i]);
						}
						$scope.stopSpin();
						
						$scope.showFichaDesafiliacion = false;
						for (var i = 0; i < $scope.tCambios.length; i++) {
							var tramite = $scope.tCambios[i];
							if (tramite.tipo.codigo == '15') {
								$scope.showFichaDesafiliacion = true;
							}
						}
						
						if (response.length) $scope.tCambiosNextPage = page + 1;
						else $scope.tCambiosNextPage = -1;
					}, 
					function(response) {
						$scope.stopSpin();
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
					});
		}
		
		$scope.getTramiteDocumentos = function(page) {
			$scope.startSpin();
			AfiliadosService.findTramitesPageByTipo('Documentos', $scope.afiliadoId, page).
				query().$promise.then(	function(response) {
						for (var i = 0; i < response.length; i++) {
							$scope.tDocumentos.push(response[i]);
						}
						$scope.stopSpin();
						if (response.length) $scope.tDocumentosNextPage = page + 1;
						else $scope.tDocumentosNextPage = -1;
					}, 
					function(response) {
						$scope.stopSpin();
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
					});
		}
		
		$scope.parseTramiteData = function(tramite) {
			if (tramite.tipo.codigo == '05') {
				var data = angular.fromJson(tramite.data);
				var ret = [];
				$.each(data, function(key, value) {
					var changed;
					$.each(value, function(k, v) {
						if (key == 'categoria' || key == 'convenio') {
							var data = angular.fromJson(v);
							var nombre = data.nombre;
							
							if (k != 'null') {
								data = angular.fromJson(k);
								k = data.nombre;
							}
							
							changed = k + ' -> ' + nombre;
						} else {
							changed = k + ' -> ' + v;
						}
					})
					ret.push(key + ': ' + changed);
				});
				return ret;
			} else if (tramite.tipo.codigo == '10') {
				var data = angular.fromJson(tramite.data);
				return [data.original.nombre + ' -> ' + data.destino.nombre];
			//} else if (tramite.tipo.codigo == '30' || tramite.tipo.codigo == '35') {
			//	var data = angular.fromJson(tramite.data);
			//	return [data.relacion.nombre + ': ' + data.familiar.nombres + ' ' + data.familiar.apellidos];
			} else if (tramite.tipo.codigo == '50' || tramite.tipo.codigo == '55') {
				var data = angular.fromJson(tramite.data);
				var nivel = "Primario";
				if (data.nivel == 'S') {
					nivel = "Secundario";
				} else if (data.nivel == 'T') {
					nivel = "Terciario";
				} else if (data.nivel == 'U') {
					nivel = "Universitario";
				}
				
				return [nivel + ': ' + data.establecimiento];
			} else if (tramite.tipo.codigo.indexOf("DA") == 0) {
				var data = angular.fromJson(tramite.data);
				var ret = {
					"id": data.id,
					"nombre": data.archivo
				};
				return ret;
			} else if (tramite.tipo.codigo == '15') {
				var data = {}
				if (tramite.data) data = angular.fromJson(tramite.data);
				return data;
			}
			return 'No se pudo parsear el trámite';
		}
		
		$scope.busquedaSeleccionarMetodo = function(metodo) {
			$scope.metodoBusquedaSeleccionado = metodo;
		}
		
		$scope.isMetodoBusquedaSeleccionado = function(metodo) {
			return $scope.metodoBusquedaSeleccionado == metodo;
		}
		
		$scope.buscar = function() {
			if ($scope.user != null) {
				if ($scope.tipoDato == 'cuil') {
					$scope.startSpin();
					AfiliadosService.findByCUIL($scope.campoBusqueda).
						query().$promise.then(	function(response) {
								$scope.afiliados = response;
								$scope.stopSpin();
							}, 
							function(response) {
								$scope.stopSpin();
								if (response.status == 500) {
									$state.go('login');
								} else {
									$rootScope.showError(response.status.toString());
								}
							});
				}
				if ($scope.tipoDato == 'documento-numero') {
					$scope.startSpin();
					AfiliadosService.findByDocumento($scope.campoBusqueda).
						query().$promise.then(	function(response) {
								$scope.afiliados = response;
								$scope.stopSpin();
							}, 
							function(response) {
								$scope.stopSpin();
								if (response.status == 500) {
									$state.go('login');
								} else {
									$rootScope.showError(response.status.toString());
								}
							});
				}
				if ($scope.tipoDato == 'numero-afiliado') {
					$scope.startSpin();
					AfiliadosService.findByNumeroAfiliado($scope.campoBusqueda).
						query().$promise.then(	function(response) {
								$scope.afiliados = response;
								$scope.stopSpin();
							}, 
							function(response) {
								$scope.stopSpin();
								if (response.status == 500) {
									$state.go('login');
								} else {
									$rootScope.showError(response.status.toString());
								}
							});
				}
				
				$scope.busquedaVisible = true;
			}
		}
		
		$scope.seleccionarSeccion = function(seccion) {
			$scope.seccion = seccion;
		}
		
		$scope.seccionSeleccionada = function(seccion) {
			return $scope.seccion == seccion;
		}
		
		$scope.calcularCuil = function() {
			var xy = "";
			var z = "";
			var dni = $scope.afiliado.documento;
			if (!dni || dni.toString().length != 8) return;
			var sexo = $scope.afiliado.sexo;
			
			var numbers = [5, 4, 3, 2, 7, 6, 5, 4, 3, 2];
			
			if (sexo == 'MASCULINO') {
				xy = '20';
			} else {
				xy = '27';
			}
			
			var tmpNum = 0;
			var digits = (xy + dni).split('');
			digits.forEach(function (digit, index) {
				if (index > numbers.length -1) return;
				
				tmpNum += parseInt(digit) * numbers[index];
			});
			
			var div = Math.floor(tmpNum/11);
			var rem = tmpNum % 11;
			
			if (rem == 0) {
				z = '0';
			} else if (rem == 1) {
				if (sexo == 'MASCULINO') {
					z = '9';
					xy = '23';
				} else {
					z = '4';
					xy = '23';
				}
			} else {
				z = 11 - rem;
			}

			$scope.afiliado.cuil = xy + dni + z;
		}
		
		$scope.editar = function() {
			$scope.stopSpin();
			AfiliadosService.findLocalidades().
				get().$promise.then(	function(response) {
						//console.debug(response);
						$scope.localidades = response.data;
						$scope.stopSpin();
					}, 
					function(response) {
						$scope.stopSpin();
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
					});
			
			if ($scope.afiliadoId) {
				$scope.startSpin();
				AfiliadosService.getAfiliado($scope.afiliadoId).
					get().$promise.then(function(response) {
							$scope.parseAfiliadoResponse(response);
							$scope.stopSpin();
						}, 
						function(response) {
							if (response.status == 500) {
								$state.go('login');
							} else {
								$rootScope.showError(response.status.toString());
							}
							$scope.stopSpin();
						});
			}
		}
		
		$scope.habilitarEdicion = function() {
			$scope.modoEdicion = true;
		}
		
		$scope.guardarAfiliado = function(imprimirCarnet) {
			var afiliadoDTO = $scope.buildAfiliadoTO(imprimirCarnet);
			console.debug('AFILIADO DTO ');
			console.debug(angular.toJson(afiliadoDTO));
			
			$scope.startSpin();
			console.debug('startspin');
			AfiliadosService.saveAfiliado().saveAfiliado({}, angular.toJson(afiliadoDTO))
				.$promise.then(function(response) {
					console.debug('1');
					var newId = false;
					console.debug('2');
					if (!$scope.afiliadoId) newId = response.afiliado.id;
					console.debug('3');
					$scope.parseAfiliadoResponse(response);
					console.debug('4');
					if ($scope.uploader.flow && $scope.uploader.flow.files.length > 0) {
						console.debug('5');
						$scope.upload(newId);
					} else {
						console.debug('6');
						$scope.stopSpin();
						$rootScope.showInfo("La información se ha guardado con éxito");
						if (imprimirCarnet) {	
							var url = baseUrl + 'carnetservice/getCarnet?id=' + $scope.afiliado.id;
							var nw = window.open(url);
						}
						if (newId) $state.go('home.editar-afiliado', {"afiliadoId": newId});
					}
	            },
	            function(response) {
	            	console.debug('7');
	            	//console.debug('laaaaaaaaaaaaaaaaa');
	            	$scope.stopSpin();
	            	if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
	            });
		}
		
		$scope.pasivarAfiliado = function() {
			var afiliadoDTO = $scope.buildAfiliadoTO();

			$scope.startSpin();
			AfiliadosService.pasivarAfiliado().pasivarAfiliado({}, angular.toJson(afiliadoDTO))
				.$promise.then(function(response) {
					$scope.parseAfiliadoResponse(response);
					if ($scope.uploader.flow && $scope.uploader.flow.files.length > 0) {
						$scope.upload();
					} else {
						$scope.stopSpin();
						$rootScope.showInfo("La información se ha guardado con éxito");
					}
	            },
	            function(response) {
	            	$scope.stopSpin();
	            	if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
	            });
		}
		
		$scope.desafiliarAfiliado = function() {
			var afiliadoDTO = $scope.buildAfiliadoTO();
			var parameters = $scope.parameters;
			
			var ModalInstanceCtrl = function ($scope, $modalInstance) {
				$scope.tiposBaja = parameters.tiposBaja;
                $scope.motivo = {};
                $scope.close = function (motivo) {
                	//console.debug(motivo);
                	$modalInstance.close(motivo);
                };
            };

			var modalInstance = $modal.open({
            	  backdrop:'static',
            	  keyboard: false,
	              templateUrl: 'partials/motivo-baja.html',
	              controller: ModalInstanceCtrl,
	              resolve: {
	                  
	              }
            });

            modalInstance.result.then(function (motivo) {
            	if (motivo) {
	            	//console.debug(motivo);
	            	afiliadoDTO.afiliado.tipoBaja = motivo;
	            	//console.debug(angular.toJson(afiliadoDTO));
	            	$scope.doDesafiliarAfiliado(afiliadoDTO);
            	}
            }, function () {});
			
		}
		
		$scope.doDesafiliarAfiliado = function (afiliadoDTO) {
			$scope.startSpin();
			AfiliadosService.desafiliarAfiliado().desafiliarAfiliado({}, angular.toJson(afiliadoDTO))
				.$promise.then(function(response) {
					$scope.parseAfiliadoResponse(response);
					if ($scope.uploader.flow && $scope.uploader.flow.files.length > 0) {
						$scope.upload();
					} else {
						$scope.stopSpin();
						$rootScope.showInfo("La información se ha guardado con éxito");
					}
	            },
	            function(response) {
	            	$scope.stopSpin();
	            	if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
	            });
		}
		
		$scope.reafiliarAfiliado = function() {
			var afiliadoDTO = $scope.buildAfiliadoTO();
			
			$scope.startSpin();
			AfiliadosService.reafiliarAfiliado().reafiliarAfiliado({}, angular.toJson(afiliadoDTO))
				.$promise.then(function(response) {
					$scope.parseAfiliadoResponse(response);
					if ($scope.uploader.flow && $scope.uploader.flow.files.length > 0) {
						$scope.upload();
					} else {
						$scope.stopSpin();
						$rootScope.showInfo("La información se ha guardado con éxito");
					}
	            },
	            function(response) {
					$scope.stopSpin();
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
	            });
		}
		
		$scope.darPorAfiliado = function() {
			var afiliadoDTO = $scope.buildAfiliadoTO();
			
			$scope.startSpin();
			AfiliadosService.darPorAfiliado().darPorAfiliado({}, angular.toJson(afiliadoDTO))
				.$promise.then(function(response) {
					$scope.parseAfiliadoResponse(response);
					$scope.stopSpin();
					$rootScope.showInfo("La información se ha guardado con éxito");
	            },
	            function(response) {
					$scope.stopSpin();
					if (response.status == 500) {
						$state.go('login');
					} else {
						$rootScope.showError(response.status.toString());
					}
	            });
		}
		
		$scope.buildAfiliadoTO = function(imprimirCarnet) {
			var afiliadoDTO = {};
			afiliadoDTO.afiliado = $scope.afiliado;
			afiliadoDTO.estudios = $scope.estudios;
			afiliadoDTO.familiares = $scope.familiares;
			afiliadoDTO.tramites = $scope.tramites;
			afiliadoDTO.documentos = $scope.documentos;
			afiliadoDTO.notas = $scope.notas;
			afiliadoDTO.foto = $scope.foto;
			afiliadoDTO.imprimirCarnet = imprimirCarnet == true;
			
			return afiliadoDTO;
		}
		
		$scope.parseAfiliadoResponse = function(response) {
			$scope.afiliado = response.afiliado;
			$scope.estudios = response.estudios;
			$scope.familiares = response.familiares;
			$scope.tramites = response.tramites;
			$scope.documentos = response.documentos;
			$scope.notas = '';
			$scope.foto = null;
			$scope.stamp = Math.random();
			
			if (!$scope.afiliado.profesion) {
				$scope.afiliado.profesion = 'Empleado';
			}
			
			$scope.afiliadoId = response.afiliado.id;
			
			$scope.rereshAllTramites();
		}
		
		$scope.agregarEstudio = function() {
			$scope.estudios[$scope.estudios.length] = {
					establecimiento: "Sin datos",
					titulo: "Sin datos"
			};
		}
		
		$scope.eliminarEstudio = function(estudio) {
			var index = $scope.estudios.indexOf(estudio);
			$scope.estudios.splice(index, 1);
		}
		
		$scope.verFamiliar = function (id) {
			var familiar = null;
			for (var x = 0; x < $scope.familiares.length; x++) {
				if ($scope.familiares[x].id == id) {
					familiar = $scope.familiares[x];
				}
			}
			if (familiar) {
				$scope.editarFamiliar(familiar, true);
			}
		}
		
		$scope.editarFamiliar = function (familiar, soloLectura){
			var parameters = $scope.parameters;
			var ModalInstanceCtrl = function ($scope, $modalInstance) {
                $scope.familiar = familiar;
                $scope.modoEdicion = !soloLectura;
                $scope.clear = (familiar) ? true : false;
                $scope.parameters = parameters;
                $scope.close = function (nuevoFamiliar) {
                    $modalInstance.close(nuevoFamiliar);
                };
            };

			var modalInstance = $modal.open({
            	  backdrop:'static',
            	  keyboard: false,
	              templateUrl: 'partials/familiar.html',
	              controller: ModalInstanceCtrl,
	              resolve: {
	                  
	              }
            });

            modalInstance.result.then(function (to) {
                if(to != null && to.familiar != null){
                	var nuevoFamiliar = to.familiar; 
                	var index = $scope.familiares.indexOf(nuevoFamiliar);
                	if (index != -1) {
                		$scope.familiares.splice(index, 1);
                	}
                	
                	if (nuevoFamiliar.id) {
	                	AfiliadosService.findFamiliarDocumentos(nuevoFamiliar.id).
							query().$promise.then(	function(response) {
									nuevoFamiliar.documentos = response;
									var documentosEliminados = to.documentosEliminados;
									if (documentosEliminados && documentosEliminados.length) {
										for (var x = 0; x < documentosEliminados.length; x++) {
											var index = nuevoFamiliar.documentos.indexOf(documentosEliminados[x]);
											nuevoFamiliar.documentos.splice(index, 1);
										}
									}
								}, 
								function(response) {
									$scope.stopSpin();
									if (response.status == 500) {
										$state.go('login');
									} else {
										$rootScope.showError(response.status.toString());
									}
								});
	                	AfiliadosService.findTramites($scope.afiliado.id).
							query().$promise.then(	function(response) {
									$scope.tramites = response;
								}, 
								function(response) {
									$scope.stopSpin();
									if (response.status == 500) {
										$state.go('login');
									} else {
										$rootScope.showError(response.status.toString());
									}
								});
                	}
                	
                	$scope.familiares.push(nuevoFamiliar);
                }}, function () {});
		}
		
		$scope.eliminarFamiliar = function(familiar) {
			var index = $scope.familiares.indexOf(familiar);
			$scope.familiares.splice(index, 1);
		}
		
		$scope.notaFilter = function(tramite) {
			return tramite.tipo.codigo == "26";
		};
		
		$scope.aprobarTramite = function(tramite) {
			if ($scope.afiliadoId) {
				if (tramite.tipo.codigo == '30') { // VALIDAR DOCUMENTOS OBLIGATORIOS DE FAMILIARES
					var familiar = null;
					for (var x = 0; x < $scope.familiares.length; x++) {
						if ($scope.familiares[x].id == tramite.data) {
							familiar = $scope.familiares[x];
						}
					}
					if (familiar) {
						var obligatorios = familiar.relacion.documentosObligatorios;
						for (var x = 0; x < obligatorios.length; x++) {
							var obligatorio = obligatorios[x];
							var found = false;
							for (var i = 0; i < familiar.documentos.length; i++) {
								if (familiar.documentos[i].tipo.codigo == obligatorio.codigo) {
									found = true;
								}
							}
							if (!found) {
								$rootScope.showError("No se puede aprobar el familiar. Falta la documentación obligatoria: " + obligatorio.nombre);
								return;
							}
						}
					}
				}
				
				//console.debug(angular.toJson(tramite.tipo.documentosObligatorios));
				if (tramite.tipo.documentosObligatorios && tramite.tipo.documentosObligatorios.length) {
					for (var x = 0; x < tramite.tipo.documentosObligatorios.length; x++) {
						var obligatorio = tramite.tipo.documentosObligatorios[x];
						//console.debug(obligatorio);
						var found = false;
						for (var i = 0; i < $scope.documentos.length; i++) {
							var documento = $scope.documentos[i];
							//console.debug(obligatorio);
							if (documento.tipo.codigo == obligatorio.codigo) {
								found = true;
							}
						}
						if (!found) {
							$rootScope.showError("No se puede aprobar el trámite. Falta la documentación obligatoria: " + obligatorio.nombre);
							return;
						}
					}
				}
				
				var to = {
					'tramite': tramite,
					'nota': ''
				};
				$scope.startSpin();
				AfiliadosService.aprobarTramite().aprobarTramite({}, angular.toJson(to))
					.$promise.then(function(response) {
						$scope.parseAfiliadoResponse(response);
						
						$scope.stopSpin();
						$rootScope.showInfo("La información se ha guardado con éxito");
		            },
    	            function(response) {
    					$scope.stopSpin();
    					if (response.status == 500) {
    						$state.go('login');
    					} else {
    						$rootScope.showError(response.status.toString());
    					}
    	            });
			}
		};
		
		$scope.rechazarTramite = function(tramite) {
			if ($scope.afiliadoId) {
				var parameters = $scope.parameters;
				var ModalInstanceCtrl = function ($scope, $modalInstance) {
	                $scope.tramite = tramite;
	                $scope.parameters = parameters;
	                $scope.close = function (nota) {
	                    $modalInstance.close(nota);
	                };
	            };

				var modalInstance = $modal.open({
	            	  backdrop:'static',
	            	  keyboard: false,
		              templateUrl: 'partials/rechazar-tramite.html',
		              controller: ModalInstanceCtrl,
		              resolve: {
		                  
		              }
	            });

	            modalInstance.result.then(function (nota) {
	                if(nota && nota != null){
	                	$scope.startSpin();
	    				var to = {
	    					'tramite': tramite,
	    					'nota': nota
	    				};
	    				AfiliadosService.rechazarTramite().rechazarTramite({}, angular.toJson(to))
		    				.$promise.then(function(response) {
		    					$scope.parseAfiliadoResponse(response);
		    					
		    					$scope.stopSpin();
		    					$rootScope.showInfo("La información se ha guardado con éxito");
		    	            },
		    	            function(response) {
		    					$scope.stopSpin();
		    					if (response.status == 500) {
		    						$state.go('login');
		    					} else {
		    						$rootScope.showError(response.status.toString());
		    					}
		    	            });
	                }}, function () {});
			}
		};
	}
]);

atsaControllers.controller('RechazarTramiteController', ['$scope','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams',
      function ($scope, $rootScope, AfiliadosService, $modal, $translate, $stateParams) {
		$scope.nota = '';
		
  		$scope.guardar = function() {
  			$scope.$parent.$parent.close($scope.nota);
  		}
  	}
]);

atsaControllers.controller('FamiliarController', ['$scope', 'usSpinnerService','$rootScope', 'AfiliadosService', '$modal','$translate', '$stateParams',
    function ($scope, usSpinnerService, $rootScope, AfiliadosService, $modal, $translate, $stateParams) {
		$scope.uploader = {};
		$scope.documentosEliminados = [];
		
		$scope.startSpin = function(){
	        usSpinnerService.spin('spinner-1');
	    }
	    $scope.stopSpin = function(){
	        usSpinnerService.stop('spinner-1');
	    }
		
		$scope.guardar = function() {
			var to = {
				familiar: $scope.familiar,
				documentosEliminados: $scope.documentosEliminados
			};
			if ($scope.uploader.flow && $scope.uploader.flow.files.length > 0) {
				$scope.upload(function() {
						$scope.$parent.$parent.close(to);
					}, function (message) {
						$rootScope.showError('Error de upload: ' + message);
					});
			} else {
				$scope.$parent.$parent.close(to);
			}
		}
		
		$scope.generarCarnetFamiliar = function() {
			$scope.familiar.familiar.versionCarnet = $scope.familiar.familiar.versionCarnet + 1;
			window.location.href = "/atsa-ingresos-core-new/rest/carnetservice/generarCarnet?id=" + $scope.familiar.familiar.id;
		}
		
		$scope.validateDNI = function() {
			/*AfiliadosService.findFamiliarByDocumento($scope.familiar.familiar.documento).
				get().$promise.then(function(response) {
						if (response && response.id) {
							var confirmScope = {};
		                    confirmScope.confirmedClick = function() {
		                    	$scope.familiar.familiar = response;
		                    }
		                    $rootScope.showConfirm(confirmScope, "Ya existe una persona con ese documento. Desea cargarla?");
						}
					}, 
					function(response) {
						if (response.status == 500) {
							$state.go('login');
						} else {
							$rootScope.showError(response.status.toString());
						}
						$scope.stopSpin();
					});
			*/
		}
		
		$scope.upload = function(success, fail) {
			$scope.startSpin();
			var done = false;
			$scope.uploader.flow.on('complete', function() {
				for (var i = 0; i < $scope.uploader.flow.files.length; i++) {
					if ($scope.uploader.flow.files[i].isComplete())
						$scope.uploader.flow.removeFile($scope.uploader.flow.files[i]);
				}
				if ($scope.uploader.flow.files.length  > 0) {
					$scope.uploader.flow.upload();
				} else if (!done) {
					done = true;
					$scope.stopSpin();
					success();
				}
			});
			$scope.uploader.flow.on('error', function(message) {
				//$rootScope.showError('Error de upload: ' + message);
				$scope.stopSpin();
				fail(message);
			});
			

			$scope.uploader.flow.upload();
		}
		
		$scope.fileUploadQuery = function (flowFile, flowChunk, isTest) {
			return {'tipoDocumento': flowFile.tipoDocumento.codigo, 'familiarId': $scope.familiar.id};
		}
		
		$scope.removeFileToUpload = function(file) {
			$scope.uploader.flow.removeFile(file);
		}
				
		$scope.eliminarDocumento = function(documento) {
			var index = $scope.familiar.documentos.indexOf(documento);
			$scope.familiar.documentos.splice(index, 1);
			$scope.documentosEliminados.push(documento);
		}
	}
]);


