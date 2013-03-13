package org.fuwjin.usage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.fuwjin.wheatgrass.StandardInjector;

public class DiiocUsage {
	@Inject 
	private Injector diioc;
	
	@Inject
	private SampleConnection resource;
	
	private String[] args;
	
	public DiiocUsage(String[] args) {
		this.args = args;
	}

	public static void main(String... args) throws Exception{
		DiiocUsage usage = new Injector(new Object(){
			String name = "1.2.3.4";
		}).inject(new DiiocUsage(args));
		usage.run();
	}

	private void run() throws Exception {
		for(final String arg: args){
			Task task = new Injector(diioc, this).create(Task.class);
			task.run(arg);
		}
	}
	
	@Singleton
	public static class SampleConnection{
		private static int count;
		private String name;
		
		@Inject 
		public SampleConnection(@Named("name") String name){
			count++;
			this.name = name;
		}
		
		public void send(String packet){
			System.out.println(name+":"+count+" -> "+packet);
		}
	}
	
	public static class Task{
		@Inject 
		private SampleConnection connection;
		
		public void run(String packet){
			connection.send(packet);
		}
	}
}