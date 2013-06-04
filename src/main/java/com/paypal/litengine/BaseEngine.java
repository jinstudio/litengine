package com.paypal.litengine;

public abstract class BaseEngine<T extends Context<Tuple, Tuple>> implements Engine<T> {

	private T context;
	
	public BaseEngine() {
		super();
	}
    

	public BaseEngine<T> setContext(T context) {
		this.context = context;
		return this;
	}


	@Override
    public Tuple trigger(T context, Tuple input) {
        context.initTriggerSource(input);
        this.execute(context);
        return context.getFinalOutput();
    }
    
    @Override
	public Tuple trigger(Tuple input) {
		return this.trigger(context, input);
	}
    
    public abstract void execute(T context);

}
