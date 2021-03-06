import org.elasticsearch.client.Client
import org.elasticsearch.node.Node

import static org.elasticsearch.node.NodeBuilder.nodeBuilder

//// Groovy way
Node node = nodeBuilder().settings {
  path{
    home="D:/elk/elasticsearch-2.1.1-client-node"
  }
  cluster {
    name = "ice-elk"
  }
  node {
    client = true
  }
}.node()
// Get a usable Node Client
Client client = node.client
//Thread.sleep(60000)
// on shutdown
//node.close();

def response = client.search {
  indices "applog-01.15", "syslog-01.15"
  types "cir.sys.log", "cir.app.log"
  source {
    query {
      match_all { }
    }
  }
}.actionGet()

println response.getHits()
/*
import org.elasticsearch.action.ListenableActionFuture
import org.elasticsearch.action.index.IndexResponse
def username = "kimchy"

ListenableActionFuture<IndexResponse> responseFuture = client.index {
  index "syslog-01.15"
  type "cir.app.log"
  //id "my-id"
  source {
    user = username
    postDate = new Date()
    message = "Trying out Elasticsearch Groovy"
    nested {
      nested_object {
        some_int = 123
        some_double = 4.56
        some_object_list = [{
            key = "Closures"
          }, {
            key = "Beats"
          }, {
            key = "Bears"
        }]
      }
      favorites = Integer.MAX_VALUE
    }
  }
}
*/

/*
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
CreateIndexResponse response2 = client.admin.indices.create {
  index "icetest"
  source {
    settings {
      index {
        number_of_shards = 2
        number_of_replicas = 0
      }
    }
  }
}.actionGet()
println "create index successful."
*/

import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.Client

// ...
def testObj=[user:"ice",postDate:new Date(),here:1234,message:"this is import thing!"]
def objList=[]
for (int i=0;i<20;i++){
    def obj= new IndexRequest().with {
        index "syslog-01.15"
        type "cir.sys.log"
        //id "my_id"
        source {
          user = testObj.user
          postDate = testObj.postDate
          message = testObj.message
          here=testObj.here+i
        }
      }
    objList<<obj    
}
BulkResponse response3 = client.bulk {
  add objList
  /*add new IndexRequest().with {
    index "syslog-01.15"
    type "cir.sys.log"
    //id "my_id"
    source {
      user = testObj.user
      postDate = testObj.postDate
      message = testObj.message
      here=testObj.here
    }
  }
  */  
  /*
  add new IndexRequest().with {
    index "syslog-01.15"
    type "cir.sys.log"
    //id "my_id"
    source {
      user = "11111"
      postDate = "2013-01-30"
      message = "111trying out Elasticsearch"
      nested {
        details {
          here = 123
          timestamp = new Date()
        }
      }
    }
  }, 
  new IndexRequest().with {
    index "syslog-01.15"
    type "cir.sys.log"
    //id "my_id"
    source {
      user = "2222222"
      postDate = "2013-01-30"
      message = "111trying out Elasticsearch"
      nested {
        details {
          here = 123
          timestamp = new Date()
        }
      }
    }
  },
  new IndexRequest().with {
    index "syslog-01.15"
    type "cir.sys.log"
    //id "my_id"
    source {
      user = "333333"
      postDate = "2013-01-30"
      message = "111trying out Elasticsearch"
      nested {
        details {
          here = 123
          timestamp = new Date()
        }
      }
    }
  } 
  */   
}.actionGet()

/*
//// Purely Java way
Node node = nodeBuilder().client(true).clusterName("my-cluster-name").node()
// Get a usable Node Client
Client client = node.client()
*/

//这里是详细的如何用groovy处理es请求的例子
//https://github.com/elastic/elasticsearch-groovy/blob/master/docs/quickstart.asciidoc
