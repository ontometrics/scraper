package com.ontometrics.scraper;

/**
 * Allows the user to indicate which entities are to be found in the pages that
 * are being scraped. Then the scraper will then attempt to go to the prescribed
 * pages and pull off one or more of the prescribed entities and return them.
 * <p>
 * A simple example would be a Contact. The scraper might find a table with
 * names and email addresses and then return a set of contacts, or a page with a
 * few contacts in different sections, or perhaps just one contact.
 * <p>
 * The big ideas here are three:
 * <ul>
 * <li>We are sparing the developer of having to write a bunch of tedious code
 * that peels strings out of the page and plunks them into corresponding
 * attributes.
 * <li>We are opening the door to letting the user stipulate particulars about
 * what to extract as annotations in the target entity, e.g. the entity might
 * have a field that the extractor is not finding and a hint, like the ID of the
 * component, could be added as an annotation to the attribute in the target.
 * <li>This also opens the door to the potential for full automation, meaning,
 * users can start by just providing a base scraper and entity and then we can
 * work on making the identification of key types stronger and stronger, and
 * that type identification can be leveraged across domains.
 * </ul>
 * <p>
 * One of the questions to be worked out here is if type identification will end
 * up making its way down into the extraction layer instead.
 * 
 * @author Rob
 * 
 */
public class EntityScraper<T> {

}
