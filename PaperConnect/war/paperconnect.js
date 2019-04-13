//Function for initialize the HTML table, either for displaying single paper or list of papers
function initPaperTable(elementId, flag) {

	// Clear the table
	document.getElementById(elementId).innerHTML = "";

	// Create a table as a list 
	var tableHeader = document.createElement('LI');
	
	//Create the first row of the table, which is the table header
	var title = document.createElement("DIV");
	var author = document.createElement("DIV");
	var publishDate = document.createElement("DIV");
	var abstract = document.createElement("DIV");

	//Add className attribute field for referencing from the css
	tableHeader.className = "table-header";
	title.className = "col col-1";
	author.className = "col col-2";
	publishDate.className = "col col-3";
	abstract.className = "col col-4";

	//Set the div elements to the corresponding table headers
	title.innerHTML = "TITLE";
	author.innerHTML = "AUTHOR";
	publishDate.innerHTML = "PUBLISH DATE";
	abstract.innerHTML = "ABSTRACT";
	
	//Append the table headers to the table
	tableHeader.appendChild(title);
	tableHeader.appendChild(author);
	tableHeader.appendChild(publishDate);
	
	//If the configuration is for a single paper add an additional column for the abstract
	if(flag){
		tableHeader.appendChild(abstract);
	}
	
	//Append the table to the element in which it needs to embedded
	document.getElementById(elementId).appendChild(tableHeader);
}

//Function for adding papers to the table, either for a single paper configuration or list of papers
function addPaperToTable(obj, elementId, flag) {
	var paper = JSON.parse(obj);
	
	//Create a table row for embedding the data
	var tableRow = document.createElement('LI');
	
	//Div elements representing the table row data
	var paperTitle = document.createElement("DIV");
	var paperAuthor = document.createElement("DIV");
	var paperPublishDate = document.createElement("DIV");
	var paperAbstract = document.createElement("DIV");
	
	//Create an attribute for the data for referencing from the css
	var data_label_title = document.createAttribute("data-label");
	var data_label_author = document.createAttribute("data-label");
	var data_label_publishDate = document.createAttribute("data-label");
	var data_label_abstract = document.createAttribute("data-label");
	
	//Add the className attribute field for referencing from the css
	tableRow.className = "table-row";
	paperTitle.className = "col col-1";
	paperAuthor.className = "col col-2";
	paperPublishDate.className = "col col-3";
	paperAbstract.className = "col col-4";

	//Set the value of the attribute and add it to the appropriate div
	data_label_title.value = "Title";
	paperTitle.setAttributeNode(data_label_title);

	//Set the value of the attribute and add it to the appropriate div
	data_label_author.value = "Author";
	paperAuthor.setAttributeNode(data_label_author);

	//Set the value of the attribute and add it to the appropriate div
	data_label_publishDate.value = "Publish Date";
	paperPublishDate.setAttributeNode(data_label_publishDate);

	//Set the value of the attribute and add it to the appropriate div
	data_label_abstract.value = "Abstract";
	paperAbstract.setAttributeNode(data_label_abstract);
	
	//Set the value of the text to be displayed in the table and append it to the table row
	paperTitle.innerHTML = paper.title;
	tableRow.appendChild(paperTitle);
	
	//Set the value of the text to be displayed in the table and append it to the table row
	paperAuthor.innerHTML = paper.author;
	tableRow.appendChild(paperAuthor);

	//Set the value of the text to be displayed in the table and append it to the table row
	paperPublishDate.innerHTML = paper.publishDate;
	tableRow.appendChild(paperPublishDate);
	
	//If the configuration is for list of papers add the additional field
	if(flag){
		//Set the value of the text to be displayed in the table and append it to the table row
		paperAbstract.innerHTML = paper.abstract;
		tableRow.appendChild(paperAbstract);	
	}
	document.getElementById(elementId).appendChild(tableRow);
}

//Function for addClickHandler to every row in the table
function addRowHandlers(elementId) {
	//Retrieve the table by elementId
	var table = document.getElementById(elementId);
	
	//Retrieve the rows by tag
	var rows = table.getElementsByTagName("LI");
	
	//For every row in the table set a clickHandler to it
	for (i = 1; i < rows.length; i++) {
		//Get the current row by index
		var currentRow = rows[i];
		
		//Retrieve the data in this row by tag
		var divTags = rows[i].getElementsByTagName("DIV");
		
		//Get the first data element in this row (TITLE)
	    var titleData = divTags[0].innerHTML;
	    
	    //Create a clickHandler
	    var clickHandler = function(title) {
	        return function() {
	        	//Call the Java method onTableClick and pass in the title of the paper selected
	        	window.onTableClick(title);
	        };
	    };
	    
	    //Set the onClick method for the row to the clickHandler  
	    currentRow.onclick = clickHandler(titleData);
	}
}

function drawGraph(obj, elementId) {
	
	//Clear the graph
	document.getElementById(elementId).innerHTML = "";
	
	 // Initialize sigma:
	var s = new sigma({
		renderer : {
			container : document.getElementById(elementId),
			type : 'canvas'
		},
		settings : {}
	});
	
	s.graph.clear();
	
	//Create a JSON object store the nodes and edges to be used by sigma
	var graph = {
		nodes : [],
		edges : []
	};
	
	var jsonGraph = JSON.parse(obj);
	
	//For every node in the jsonGraph read the data and put it in graph
	for (i = 0; i < jsonGraph.nodes.length; i++) {
		//Push a new node containing the necessary fields into the nodes list
		graph.nodes.push({
			id : jsonGraph.nodes[i].id,
			label : jsonGraph.nodes[i].label,
			x : jsonGraph.nodes[i].x,
			y : jsonGraph.nodes[i].y,
			size : jsonGraph.nodes[i].size,
			color: '#008CC2'
		});
	}
	
	//For every edge in the jsonGraph read the data and put in graph
	for (i = 0; i < jsonGraph.edges.length; i++) {
		//Push a new edge containing the necessary fields into the edges list
		graph.edges.push({
			id : jsonGraph.edges[i].id,
			source : jsonGraph.edges[i].source,
			target : jsonGraph.edges[i].target,
			 color: '#DC143C',
		});
	}

	// Load the graph in sigma
	s.graph.read(graph);
	
	// Ask sigma to draw it
	s.refresh();
}