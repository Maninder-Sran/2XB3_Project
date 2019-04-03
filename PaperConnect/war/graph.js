function drawGraph(obj) {
	document.getElementById('graphContainer').innerHTML = "";	// Initialize sigma:
	var s = new sigma({
		renderer : {
			container : document.getElementById('graphContainer'),
			type : 'canvas'
		},
		settings : {}
	});
	
	s.graph.clear();
	
	var graph = {
		nodes : [],
		edges : []
	};
	var jsonGraph = JSON.parse(obj);
	console.log(obj.toString());
		
	for(i = 0; i < jsonGraph.nodes.length; i++){
		graph.nodes.push({
			id : jsonGraph.nodes[i].id,
			label : jsonGraph.nodes[i].label,
			x : jsonGraph.nodes[i].x,
			y : jsonGraph.nodes[i].y,
			size : jsonGraph.nodes[i].size,
		});
	}
	for (i = 0; i < jsonGraph.edges.length; i++) {
		console.log(jsonGraph.edges[i].source);
		console.log(jsonGraph.edges[i].target);
		graph.edges.push({
			id : jsonGraph.edges[i].id,
			source : jsonGraph.edges[i].source,
			target : jsonGraph.edges[i].target,
		});
	}

	// Load the graph in sigma
	s.graph.read(graph);
	// Ask sigma to draw it
	s.refresh();
}