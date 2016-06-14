<h1 style="color: #5e9ca0;">Multiple Views - Recyclerview-CardView</h1>
<p><strong>Libraries used</strong></p>
<ul>
<li>RecyclerView</li>
<li>CardView</li>
<li>GooglePlay Services</li>
<li>Volley</li>
<li>Jackson</li>
</ul>
<p><br /><strong>Project Features Description</strong></p>
<ul>
<li>Google Play Services​- To find out user location</li>
<li>Jacskon​- A library to deserialize JSON data using Object Mapper</li>
<li>Volley​- To start the request to get the JSON data using custom
<ul>
<li>Volley is faster than using AysncTask</li>
<li>It takes care of network handling internally</li>
<li>Caches data</li>
<li>Allows us to cancel requests attached to a Tag</li>
<li>It allows us to create a Custom Request class to execute our request</li>
</ul>
</li>
<li>I have created a custom request class for using in Volley called ​JacksonRequest<br />which extends JSONRequest</li>
<li>JacksonRequest​allows me to get the JSON response I need using volley and<br />Jackson&rsquo;s Object mapper.</li>
<li>Using RecyclerView and CardView for smooth scrolling and view re-usability</li>
<li>A ViewType and a ViewHolder for each card type so that different card types can be<br />added in the future.
<ul>
<li>Used a LinkedHashSet to eliminate overlapping content</li>
</ul>
</li>
</ul>
<p><strong>Additional Functionality</strong><br />1. Cards can be re-ordered by a long press and drag<br />2. Cards can be swiped right to delete a card<br />3. Infinite scrolling is implemented. To show infinite scrolling scrolling i have created a few cards of the &lsquo;Place&rsquo; type<br /><br /></p>
<p>&nbsp;</p>
<p><strong>Q: How would your implementation handle adding 20 more types (e.g. places, movies, events, videos, travel, etc)?</strong></p>
<ul>
<li>Add a new ViewType</li>
<li>Create a new ViewHolder and the corresponding layout</li>
<li>Hook it all up in the adapter</li>
</ul>
<p><strong>Q: How does adding 20 more types affect the performance of the app?</strong></p>
<ul>
<li>Recycling views improves performance by avoiding the creation of unnecessary views or performing expensive findViewById() lookups.</li>
<li>For each type of card created the method onCreateViewHolder() is called.</li>
<li>Each call to onCreateViewHolder() leads to creation of a new instance of&nbsp;ViewHolder</li>
<li>If there are 20 types and 1 item of each type in the list,&nbsp;onCreateViewHolder() will be called 20 times to create a view for every item.</li>
<li>If there is 1 type and 20 items of each type, depending on the screen size,&nbsp;lets 7 items fit on the screen, then o​nCreateViewHolder() will be called&nbsp;approximately 8 &shy;to 9 times.</li>
<li>Also, onCreateViewHolder() is not called when we view already created<br />items, eg. while scrolling up/back in a list</li>
<li>Having too many types defeats the purpose of ViewHolder pattern.&nbsp;It does not allow us to reuse the views as we don&rsquo;t have access to s​crap views&nbsp;which are cached</li>
</ul>
<p><strong>Q: What if these cards are visible in many different locations in the app?</strong></p>
<ul>
<li>To use these cards across multiple lists we can use RecycledViewPool<br />RecycledViewPool lets us share Views between multiple RecyclerViews.</li>
<li>If you want to recycle views across RecyclerViews, we create an instance of&nbsp;RecycledViewPool and use setRecycledViewPool(RecycledViewPool)</li>
<li>RecyclerView automatically creates a pool for itself if you don't provide one.</li>
<li>This has a method setMaxRecycledViews</li>
</ul>
