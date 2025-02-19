package com.falon.feed.presentation.model

internal data class ProjectSharedElementKey(
    val projectId: String,
    val type: ProjectSharedElementType
)

internal enum class ProjectSharedElementType {
    AvatarImage,
    Title,
    Description,
    Stars,
    StarImage,
}
