

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'metseditor.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'metseditor.UserRole'
grails.plugin.springsecurity.authority.className = 'metseditor.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['isAuthenticated()']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/zkapp/**', access: ['isAuthenticated()']],
	[pattern: '/zkau/**', access: ['isAuthenticated()']],
	[pattern: '/user/**', access: ['ROLE_ADMIN']],
	[pattern: '/task/**', access: ['ROLE_ADMIN']],
	[pattern: '/book/**', access: ['ROLE_ADMIN','ROLE_TYPIST','ROLE_PROOFREADER']],
	[pattern: '/typing/**', access: ['ROLE_ADMIN','ROLE_TYPIST']],
	[pattern: '/proofread1/**', access: ['ROLE_ADMIN','ROLE_PROOFREADER']],
	[pattern: '/proofread2/**', access: ['ROLE_ADMIN','ROLE_PROOFREADER']],
	[pattern: '/proofread3/**', access: ['ROLE_ADMIN','ROLE_PROOFREADER']],
	[pattern: '/*', access: ['isAuthenticated()']],
	[pattern: '/PageImage/**',access: ['isAuthenticated()']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/index.zul'
