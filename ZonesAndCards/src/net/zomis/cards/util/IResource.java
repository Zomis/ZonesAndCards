package net.zomis.cards.util;

public interface IResource {
	// Do NOT involve annotations in any way.
	// Remember that even though it would be possible to annotate this on a field or whatever, there has to be an IResource somewhere.
	// Consider especially ENUMs that implements IResource

	ResourceData createData(IResource resource);
}
