package com.common.evebusmodel;
/**
 * EventBus框架
 * 
 *<pre>
 *  &lt; 框架优点 
 *  &lt; 传统的事件传递方式包括：Handler、BroadCastReceiver、Interface 回调，相比之下 EventBus 的优点是代码简洁，使用简单，并将事件发布和订阅充分解耦
 *  &lt; 
 *  &lt;   EventBus是一个Android端优化的publish/subscribe消息总线，简化了应用程序内各组件间、
 *  &lt; 组件与后台线程间的通信。比如请求网络，等网络返回时通过Handler或Broadcast通知UI，两个Fragment
 *  &lt; 之间需要通过Listener通信，这些需求都可以通过 EventBus 实现。
 *  &lt; 
 *  &lt; AndroidStudio 可以添加compile 'de.greenrobot:eventbus:3.0.0-beta1'
 *  &lt; 
 *  &lt;   注意：我们添加了注解@Subscribe，其含义为订阅者，在其内传入了threadMode，我们定义为ThreadMode.
 *  &lt; MainThread，其含义为该方法在UI线程完成，这样你就不要担心抛出异常。
 *  &lt; 
 *</pre>
 *   
 */
public class AddCategoryEvent {
	public boolean bool;
	
	public AddCategoryEvent(Boolean bool){
		this.bool = bool;
	}

	public boolean isBool() {
		return bool;
	}

}
