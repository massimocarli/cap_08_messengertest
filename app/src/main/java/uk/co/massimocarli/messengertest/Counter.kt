package uk.co.massimocarli.messengertest

/**
 * Abstraction for the Service we need
 */
interface Counter {

  /**
   * Starts the counter
   */
  fun startCounter()

  /**
   * Stops the counter
   */
  fun stopCounter()
}