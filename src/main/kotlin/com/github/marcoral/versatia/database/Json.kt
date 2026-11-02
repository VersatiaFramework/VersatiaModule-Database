package com.github.marcoral.versatia.database

/** JSON text for a `{json}` column; bound so that PostgreSQL's JSONB accepts it, plain text elsewhere. */
@JvmInline
value class Json(val text: String)
