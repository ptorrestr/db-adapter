Adapter for DataBase

for run!

1)
s4 zkServer -clean

2)
./s4 newCluster -c=cluster1 -nbTasks=2 -flp=12000; ./s4 newCluster -c=cluster2 -nbTasks=1 -flp=13000

3)
./s4 node -c=cluer1
./s4 node -c=cluer1

4)
s4 deploy -appName=db-adapter-own -appClass=hujo.adapter.TweetApp -b=`pwd`/build.gradle -c=cluster1

5)
s4 node -c=cluster2 -p=s4.apter.output.stream=RawStatus

6)
s4 deploy -appName=db-adapter-own -appClass=hujo.adapter.DbInputAdapter -b=`pwd`/build.gradle -c=cluster2
