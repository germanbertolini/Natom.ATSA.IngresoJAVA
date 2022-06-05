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
                            $scope.fechaBaja = "";
                            $scope.motivo = {};

                            $scope.close = function (motivo, fechaBaja) {
                                    $modalInstance.close({ tipoBaja: motivo, fechaBaja: fechaBaja });
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

            modalInstance.result.then(function (data) {
            	if (data.tipoBaja && data.fechaBaja) {
                    afiliadoDTO.afiliado.tipoBaja = data.tipoBaja;
                    afiliadoDTO.afiliado.fechaBaja = data.fechaBaja;

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
                        //GERMAN 2022-06-05: Le mando un flag al modal de si ya existe un beneficiario de seguro de vida
                        var tieneBeneficiarioSeguroVida = false;
                        for (var g = 0; g < $scope.familiares.length; g ++) {
                            if ($scope.familiares[g].relacion.id === 10) {
                                tieneBeneficiarioSeguroVida = true;
                                beneficiarioPersonaId = $scope.familiares[g].familiar.id;
                                break;
                            }
                        }
                        $scope.parameters.tieneBeneficiarioSeguroVida = tieneBeneficiarioSeguroVida;
                        $scope.parameters.beneficiarioPersonaId = beneficiarioPersonaId;
                        
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
		
                //GERMAN 2022-06-05: Mando a imprimir el formulario de seguro de vida
                $scope.imprimirSeguroVida = function(familiar) {
                    var newWindow = window.open();
                    newWindow.document.write(buildSeguroVidaReportHtml(familiar));
		};
                
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
                        
                        //GERMAN 05/06/2022 -> Verificamos que ya no exista un BENEFICIARIO SEGURO DE VIDA en la lista de familiares!
                        if (to.familiar.relacion.id === 10) {
                            var yaExisteBeneficiarioSeguroVida = document.getElementById('tieneBeneficiarioSeguroVida').value === 'true';
                            var beneficiarioPersonaId = parseInt(document.getElementById('beneficiarioPersonaId').value);
                            
                            if (yaExisteBeneficiarioSeguroVida && to.familiar.familiar.id !== beneficiarioPersonaId) {
                                $rootScope.showError('Ya existe un beneficiario de seguro de vida para este afiliado.');
                                return;
                            }
                        }
                        
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

//GERMAN 2022-06-05: Genero el HTML del formulario de Seguro de Vida
function buildSeguroVidaReportHtml(familiar) {
    var html = '<!DOCTYPE html>';
    html += '<html>';
    html += '<head>';
    html += '    <meta name="viewport" content="width=device-width" />';
    html += '    <title>Seguro de vida</title>';
    html += '</head>';
    html += '<body>';
    html += '    <table border="1" cellspacing="0" width="700">';
    html += '        <thead>';
    html += '            <tr>';
    html += '                <th colspan="3">';
    html += '                    <img width="80" src="data:image/png;base64,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" />';
    html += '                    <span>SEGURO DE VIDA COLECTIVO ATSA HURLINGHAM</span>';
    html += '                    <img width="80" src="data:image/png;base64,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" />';
    html += '                </th>';
    html += '            </tr>';
    html += '        </thead>';
    html += '        <tbody>';
    html += '            <tr>';
    html += '                <td colspan="2">';
    html += '                    <span style="font-weight:bold; font-size:20px">DATOS PERSONALES DEL TITULAR</span>';
    html += '                </td>';
    html += '                <td>';
    html += '                    <span style="font-weight:bold; font-size:20px">N° DE AFILIADO</span>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">APELLIDO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.afiliado.apellidos || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">NOMBRES</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.afiliado.nombres || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">TIPO Y N° DE DOCUMENTO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.afiliado.documentoTipo || "").toUpperCase() + ' ' + (familiar.afiliado.documento || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">FECHA DE NACIMIENTO</span>';
    html += '                    <br/>';
    html += '                    <b>' + getFormatoFecha(familiar.afiliado.fechaNacimientoString || "") + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">SEXO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.afiliado.sexo || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">N° AFILIADO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.afiliado.numeroAfiliado || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td colspan="3">';
    html += '                    <span style="font-weight:bold; font-size:20px">DATOS PERSONALES DEL BENEFICIARIO ASIGNADO POR TITULAR</span>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">APELLIDO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.apellidos || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">NOMBRES</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.nombres || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">TIPO Y N° DE DOCUMENTO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.documentoTipo || "").toUpperCase() + ' ' + (familiar.familiar.documento || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">FECHA DE NACIMIENTO</span>';
    html += '                    <br/>';
    html += '                    <b>' + getFormatoFecha(familiar.familiar.fechaNacimientoString || "") + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">SEXO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.sexo || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td>';
    html += '                    <span style="font-size:10px">DOMICILIO:</span>';
    html += '                    <b>' + (familiar.familiar.domicilio || "").toUpperCase() + '</b>';
    html += '                    <hr style="border-bottom:1px solid black" />';
    html += '                    <span style="font-size:10px">LOCALIDAD:</span>';
    html += '                    <b>' + (familiar.familiar.localidad || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td colspan="3">';
    html += '                    <span style="font-size:10px">N° DE TELEFONO:</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.telefono || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">PARENTESCO</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.parentesco || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">ESTADO CIVIL</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.estadoCivil || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '                <td style="text-align:center">';
    html += '                    <span style="font-size:10px">OCUPACION</span>';
    html += '                    <br/>';
    html += '                    <b>' + (familiar.familiar.profesion || "").toUpperCase() + '</b>';
    html += '                </td>';
    html += '            </tr>';
    html += '            <tr>';
    html += '                <td style="text-align:center;border-right:none;padding-right:25px;padding-left:25px">';
    html += '                    <hr style="border-top:none" />';
    html += '                    <b>' + getCurrentDate() + '</b>';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border:1px solid black" />';
    html += '                    <span style="font-size:10px">FECHA</span>';
    html += '                </td>';
    html += '                <td colspan="2" style="text-align:center;border-left:none;padding-right:25px;padding-left:25px">';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border-top:none" />';
    html += '                    <hr style="border:1px solid black" />';
    html += '                    <span style="font-size:10px">FIRMA Y ACLARACION DEL TITULAR</span>';
    html += '                </td>';
    html += '            </tr>';
    html += '        </tbody>';
    html += '    </table>';
    html += '</body>';
    html += '</html>';

    return html;
}

function getFormatoFecha(fechaString) {
    var partes = fechaString.split('-');
    if (partes.length !== 3)
        return "";
    else
        return partes[2] + "/" + partes[1] + "/" + partes[0];;
}

function getCurrentDate() {
    return (new Date().getDate().toString().padStart(2, '0')) + "/" + (new Date().getMonth() + 1).toString().padStart(2, '0') + "/" + (new Date().getFullYear());
}