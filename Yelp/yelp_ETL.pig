yelp = LOAD '/user/cloudera/project/yelp.csv';
distinct_yelp = DISTINCT yelp;
STORE distinct_yelp INTO '/user/cloudera/project/distinct_yelp.csv';