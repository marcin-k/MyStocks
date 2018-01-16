package com.marcin_k.mystocks.events;
/**
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * Only used for constant definition
 */

public interface MyEventConstants {

  // topic identifier for all topics
  String TOPIC_STOCKS = "TOPIC_STOCKS";
  
  // this key can only be used for event registration, you cannot
  // send out generic events
  String TOPIC_STOCKS_ALLTOPICS = "TOPIC_STOCKS/*";

  String TOPIC_STOCKS_MYPORTFOLIO = "TOPIC_STOCKS/MYPORTFOLIO";

} 