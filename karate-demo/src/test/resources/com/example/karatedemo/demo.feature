Feature: color

Background:
	* def colorEndpoint = karate.properties['server.url'] + '/colors'

Scenario: add and get color

	Given url colorEndpoint
	When method get
	Then status 200
	And match response == []

	Given url colorEndpoint
	And request { name: 'red' }
	When method post
	Then status 200
	And match response == { id: '#notnull', name: 'red' }

	* def redId = response.id

	Given url colorEndpoint
	When method get
	Then status 200
	And match response == [{ id: #(redId), name: 'red' }]
