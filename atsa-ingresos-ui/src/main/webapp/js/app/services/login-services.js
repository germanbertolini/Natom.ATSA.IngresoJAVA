'use strict';

atsaServices.factory('LoginService', ['$resource', function($resource){

    return {
        login: function(user) {
            var url = baseUrl + 'loginservice/:action';
            return $resource(url, {},
                {
                    authenticate: {
                        method: 'POST',
                        params: {'action' : 'authenticate'},
                        headers: {
                            'username' : user.username,
                            'password' : user.password
                        }
                    }
                }
            );
        }
    }
}]);
