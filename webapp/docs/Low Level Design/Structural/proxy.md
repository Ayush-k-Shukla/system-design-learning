# Proxy Design Pattern

- It is a structural design pattern, which acts in between of real class and you client. it is similar to prxoy we use in server architecture.

## Problem

- We have a scenario when we have to construct a resource heavy object everytime event before it is not needed. In that case i will have to implement a lazy loading so that i can initialize only when needed.
- In that case one this we can do is change my object class logic but it is not possible in all cases, this can be possible that this object is part of a closed 2rd-party lib.

## Solution

- The Proxy pattern suggest to create a new proxy class that sits in between of your object and client.
- It implments same interface of object and deletegates call to 3rd party service itself. but it has some logic in between to do.

<img src="/img/lld/proxy-1.png"/>

## Structure

<img src="/img/lld/proxy-3.png"/>

- The **Service Interface** is the interface of service and proxy should implement it so that proxy will also be treated as a object of service.
- The **Service** is normal service class.
- The **Proxy** class has a refrence field that point to real service object. It can build on top extra func like caching, logging, access control. at last it passes request to service.
- Client is a normal service which accept real service object to perform some op.

## Implementation

This example illustrates how the Proxy pattern can help to introduce lazy initialization and caching to a 3rd-party YouTube integration library.

<img src="/img/lld/proxy-2.png"/>

Below id pseudo implementation for this above diagram

```js
// The interface of a remote service.
interface ThirdPartyYouTubeLib is
    method listVideos()
    method getVideoInfo(id)
    method downloadVideo(id)

// Real service implementation
class ThirdPartyYouTubeClass implements ThirdPartyYouTubeLib is
    method listVideos() is
        // Send an API request to YouTube.

    method getVideoInfo(id) is
        // Get metadata about some video.

    method downloadVideo(id) is
        // Download a video file from YouTube.

// Proxy class with caching support
class CachedYouTubeClass implements ThirdPartyYouTubeLib is
    private field service: ThirdPartyYouTubeLib
    private field listCache, videoCache
    field needReset

    constructor CachedYouTubeClass(service: ThirdPartyYouTubeLib) is
        this.service = service

    method listVideos() is
        if (listCache == null || needReset)
            listCache = service.listVideos()
        return listCache

    method getVideoInfo(id) is
        if (videoCache == null || needReset)
            videoCache = service.getVideoInfo(id)
        return videoCache

    method downloadVideo(id) is
        if (!downloadExists(id) || needReset)
            service.downloadVideo(id)

// Client
class YouTubeManager is
    protected field service: ThirdPartyYouTubeLib

    constructor YouTubeManager(service: ThirdPartyYouTubeLib) is
        this.service = service

    method renderVideoPage(id) is
        info = service.getVideoInfo(id)
        // Render the video page.

    method renderListPanel() is
        list = service.listVideos()
        // Render the list of video thumbnails.

    method reactOnUserInput() is
        renderVideoPage()
        renderListPanel()

// The application can configure proxies on the fly.
class Application is
    method init() is
        aYouTubeService = new ThirdPartyYouTubeClass()
        aYouTubeProxy = new CachedYouTubeClass(aYouTubeService)
        manager = new YouTubeManager(aYouTubeProxy)
        manager.reactOnUserInput()
```

## Application

- Lazy initialization (virtual proxy). This is when you have a heavyweight service object that wastes system resources by being always up, even though you only need it from time to time.
- Access control (protection proxy). This is when you want only specific clients to be able to use the service object; for instance, when your objects are crucial parts of an operating system and clients are various launched applications (including malicious ones).
- Logging requests (logging proxy). This is when you want to keep a history of requests to the service object.
- Caching request results (caching proxy). This is when you need to cache results of client requests and manage the life cycle of this cache, especially if results are quite large.

## Pros and Cons

### Pros

- Can control service without client event know
- Can manage whole lifecycle of service
- Support Open/Closed principle. You can introduce new proxies without changing the service.

### Cons

- May be extra complicated code.
