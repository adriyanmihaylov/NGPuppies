angular.module('NGPuppies').config(function($stateProvider,$urlRouterProvider,$locationProvider) {
	
	// the ui router will redirect if a invalid state has come.
	$urlRouterProvider.otherwise('/page-not-found');

	$stateProvider.state('nav', {
		abstract : true,
		views : {
			'nav@' : {
				templateUrl : 'app/views/nav.html',
				controller : 'NavController'
			}
		}
	}).state('login', {
		parent : 'nav',
		url : '/login',
		views : {
			'content@' : {
				templateUrl : 'app/views/login.html',
				controller : 'LoginController'
			}
		}
	}).state('admins', {
		parent : 'nav',
		url: '/admins',
		data : {
			role : 'ROLE_ADMIN'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/admin.html',
				controller : 'UsersController',
			}
		}
	}).state('home', {
		parent : 'nav',
		url : '/',
		views : {
			'content@' : {
				templateUrl : 'app/views/home.html',
				controller : 'HomeController'
			}
		}
	}).state('page-not-found', {
		parent : 'nav',
		url : '/page-not-found',
		views : {
			'content@' : {
				templateUrl : 'app/views/page-not-found.html',
				controller : 'PageNotFoundController'
			}
		}
	}).state('access-denied', {
		parent : 'nav',
		url : '/access-denied',
		views : {
			'content@' : {
				templateUrl : 'app/views/access-denied.html',
				controller : 'AccessDeniedController'
			}
		}
	}).state('register', {
		parent : 'nav',
		url : '/register',
		views : {
			'content@' : {
				templateUrl : 'app/views/register.html',
				controller : 'RegisterController'
			}
		}

	}).state('addBill', {
        parent: 'nav',
        url: '/bill',
        views: {
            'content@': {
                templateUrl: 'app/views/add-bill.html',
                controller: 'addBillController'
            }
        }
    }).state('clients', {
            parent: 'nav',
            url: '/clients',
            views: {
                'content@': {
                    templateUrl: 'app/views/clients.html',
                    controller: 'clients'
                }
            }
    }).state('payBill', {
        parent : 'nav',
        url : '/payBill',
        views : {
            'content@' : {
                templateUrl : 'app/views/pay-bills.html',
                controller : 'payBillController'
            }
        }
    }).state('reports', {
            parent : 'nav',
            url : '/reports',
            views : {
                'content@' : {
                    templateUrl : 'app/views/reports.html',
                    controller : 'reportsCotroller'
                }
            }
    }).state('subscribers', {
        parent : 'nav',
        url : '/subscribers',
        views : {
            'content@' : {
                templateUrl : 'app/views/subscribers.html',
                controller : 'subscribersController'
            }
        }
    })
        .state('newSubscriber', {
            parent : 'nav',
            url : '/newSubscriber',
            views : {
                'content@' : {
                    templateUrl : 'app/views/new-subscriber.html',
                    controller : 'newSubscriberController'
                }
            }
        })
        .state('services', {
            parent : 'nav',
            url : '/services',
            views : {
                'content@' : {
                    templateUrl : 'app/views/services.html',
                    controller : 'servicesController'
                }
            }
        })
        .state('currencies', {
            parent : 'nav',
            url : '/currencies',
            views : {
                'content@' : {
                    templateUrl : 'app/views/currencies.html',
                    controller : 'currenciesController'
                }
            }
        })
        .state('changePassword', {
            parent : 'nav',
            url : '/changepassword',
            views : {
                'content@' : {
                    templateUrl : 'app/views/changePassword.html',
                    controller : 'changepasswordController'
                }
            }
        });
        $locationProvider.html5Mode({
			enabled: true,
			requireBase: true
        });
});
